package sparky

import spark.{Request, Response}
import spray.json._
import DefaultJsonProtocol._

import collection.mutable.ListBuffer
import collection.mutable.Map
import collection.immutable.TreeMap
import sys.process._

object Formatters extends DefaultJsonProtocol {
  implicit val jobFormatter = jsonFormat6(Job)
  implicit val tsValueFormatter = jsonFormat2(TsValue)
  implicit val clusterBandwidth = jsonFormat4(Bandwidth)
}

object App extends SparkScaffolding {
  def main(args: Array[String]): Unit = {

    val hosts: Map[String, Map[String, ListBuffer[TsValue]]] = Map()
    val threads: Map[String, Map[String, CpuThread]] = Map()

    val thread2 = new Thread {
      override def run(): Unit = {
        val cmd = Seq("tail", "-n", "100000", "-f", "log_file")
        cmd.lineStream foreach { case line =>
          val ev = line.parseJson.asJsObject.getFields("e", "host").map(_.toString.replaceAll("\"", ""))
          println(line)
          val e = ev(0)
          println(e)
          e match {
            case "SparkListenerExecutorAdded" =>
              val host = ev(1)
              val cpuInfo = new ListBuffer() ++= Seq(TsValue(0, 0))
              val cpuThread = new CpuThread(cpuInfo, host)
              cpuThread.start
              threads += (host -> Map( "thread" -> cpuThread))
              hosts += (host -> Map("cpu"-> cpuInfo))
            case "SparkListenerExecutorRemoved" =>
              val host = ev(1)
              threads.lift(host).flatMap(_.get("thread")).foreach(_.stop)
              threads -= (host)
              hosts -= (host)
            case _ =>
              println("no match for " + e)
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
      State.activeJobs.toJson
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
      tmap.values.toJson
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
        while (cpuInfo.length >  12)
          cpuInfo.remove(0)
        val cpu = line.split(":").lift(2).map(_.toDouble).getOrElse(-3.0)
        val ts = line.split(" ").lift(0).map(_.toLong).getOrElse(30L)
        cpuInfo += TsValue(ts, cpu)
        println(cpuInfo)
    }
  }
}
