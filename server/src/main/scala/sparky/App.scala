package sparky
import spark.{Request, Response}
import spray.json._
import DefaultJsonProtocol._

import collection.mutable.ListBuffer
import collection.mutable.Map
import collection.immutable.TreeMap
import scala.collection.JavaConverters._
import sys.process._
import java.util.concurrent.ConcurrentHashMap

object Formatters extends DefaultJsonProtocol {
  implicit val jobFormatter = jsonFormat8(Job)
  implicit val tsValueFormatter = jsonFormat2(TsValue)
  implicit val clusterBandwidth = jsonFormat4(Bandwidth)
}

object App extends SparkScaffolding {
  def str(obj: JsObject, key: String): String =
    obj
      .getFields(key)
      .headOption
      .map(_.toString.replaceAll("\"", ""))
      .getOrElse("")

  def long(obj: JsObject, key: String): Long =
    obj
      .getFields(key)
      .headOption
      .map(_.toString.toLong)
      .getOrElse(-1L)


  val jobs = new ConcurrentHashMap[Long, Job]

  def main(args: Array[String]): Unit = {

    val hosts: Map[String, Map[String, ListBuffer[TsValue]]] = Map()
    val threads: Map[String, Map[String, CpuThread]] = Map()

    val thread2 = new Thread {
      override def run(): Unit = {
        val cmd = Seq("tail", "-n", "+1", "-f", "log_file")
        cmd.lineStream foreach { case line =>
          val obj = line.parseJson.asJsObject

          val e = str(obj, "e")
          val host = str(obj, "host")
          println(line)
          e match {
            case "SparkListenerExecutorAdded" =>
              val cpuInfo = new ListBuffer[TsValue]()
              val cpuThread = new CpuThread(cpuInfo, host)
              cpuThread.start
              threads += (host -> Map( "thread" -> cpuThread))
              hosts += (host -> Map("cpu"-> cpuInfo))
            case "SparkListenerJobStart" =>
              val desc = str(obj, "desc")
              val ts = long(obj, "ts")
              val id = long(obj, "id")
              val stages = obj.fields("stages").asInstanceOf[JsArray]
              println(stages.elements)
              val tasksTotal: Long = stages.elements.map(_.asInstanceOf[JsObject]).map(long(_, "task_count")).sum

              jobs.put(id, Job(id, ts, 0, desc, 0, 0, 0, tasksTotal))
            case "SparkListenerJobEnd" =>
              val ts = long(obj, "ts")
              val id = long(obj, "id")

              val old = jobs.get(id)
              jobs.put(id, old.copy(finishMs = ts))
            case "SparkListenerExecutorRemoved" =>
              threads.lift(host).flatMap(_.get("thread")).foreach(_.stop)
              threads -= (host)
              hosts -= (host)
            case _ =>
              //println("no match for " + e)
          }
        }
      }
    }
    thread2.start

    import Formatters._

    get("/hello"){ (req: Request, res: Response) => Map("a" -> 1, "b" -> 2).toMap.toJson }
    get("/host/:host") { (req: Request, res: Response) =>
      hosts.get(req.params(":host")).getOrElse(Map()).toMap.map {
        case ("cpu", value) => ("cpu", value.toSeq)
        case  (key, value) => (key, value.toSeq)
      }.toJson
    }


    println("Ready!")

    get("/active-jobs"){ (req: Request, res: Response) =>
      jobs.asScala.values.toJson
    }

    get("/cluster-cpu"){ (req: Request, res: Response) =>
      val tsMap:Map[Long, TsValue] = Map()
      val normalizer = hosts.size

      hosts.foreach{case (host, y) => y.lift("cpu").foreach{buffer =>
        buffer.foreach { x =>
          tsMap += (x.ts -> TsValue(x.ts, x.value/normalizer + tsMap.lift(x.ts).map(_.value).getOrElse(0.0)))
        }
      }}
      val tmap: TreeMap[Long, TsValue] = TreeMap((0L, TsValue(0L, 0.0))) ++ tsMap
      tmap.drop(1).values.toJson
    }

    get("/cluster-bandwidth"){ (req: Request, res: Response) =>
      State.clusterBandwidth.toJson
    }
  }
}
class CpuThread(cpuInfo: ListBuffer[TsValue], host: String) extends Thread {
  override def run(): Unit = {
    MSsh.runScriptOnMachine("cpu.sh", host) foreach {
      case line =>
        println(line)
        while (cpuInfo.length >  120)
          cpuInfo.remove(0)
        val cpu = line.split(":").lift(2).map(_.toDouble).getOrElse(-3.0)
        val ts = line.split(" ").lift(0).map(_.toLong).getOrElse(30L)
        cpuInfo += TsValue(ts, cpu)
        println(cpuInfo)
    }
  }
}
