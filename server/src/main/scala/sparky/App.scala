package sparky

import spark.{Request, Response}
import spray.json._
import DefaultJsonProtocol._

import collection.mutable.ListBuffer
import collection.mutable.Map
import sys.process._

object Formatters extends DefaultJsonProtocol {
  implicit val jobFormatter = jsonFormat6(Job)
  implicit val tsValueFormatter = jsonFormat2(TsValue)
  implicit val clusterBandwidth = jsonFormat4(Bandwidth)
}

object App extends SparkScaffolding {
  def main(args: Array[String]): Unit = {

    val host = "172.30.5.7"
    val cpuInfo = new ListBuffer() ++= Seq(0, 0.1, 0.2, 0.3, 0.4)

    val thread = new Thread {
      override def run(): Unit = {
        MSsh.runScriptOnMachine("cpu.sh", host) foreach {
          case line =>
            println(line)
            cpuInfo.remove(0)
            cpuInfo += line.split(":").lift(2).map(_.toDouble).getOrElse(-3.0)
            println(cpuInfo)
        }
      }
    }
    thread.start()

    val hosts = Map(host -> Map("cpu"-> cpuInfo))

    val thread2 = new Thread {
      override def run(): Unit = {
        val cmd = Seq("tail", "-n", "100000", "-f", "log_file")
        cmd.lineStream foreach { case line =>
          val ev = line.parseJson.asJsObject.getFields("e", "host")
          println(line)
          ev match {
            case "SparkListenerExecutorAdded", host => 
              val cpuInfo = new ListBuffer() ++= Seq(0, 0.1, 0.2, 0.3, 0.4)
              hosts += (host.toString -> Map("cpu"-> cpuInfo))
            case "SparkListenerExecutorAdded", host =>
              hosts -= (host.toString)
          }
          if (ev(0) == ) {
            hosts += (ev(1).toString -> Map("cpu"-> cpuInfo))
          } else if (ev(0) == ) {
            hosts -= (ev(1).toString)
          } else {

          }
        }
      }
    }
    thread2.start

    get("/hello"){ (req: Request, res: Response) => Map("a" -> 1, "b" -> 2).toMap.toJson }
    get("/host/:host") { (req: Request, res: Response) =>
      hosts.get(req.params(":host")).getOrElse(Map()).toMap.map {
      case ("cpu", value) => ("cpu", value.toSeq)
      case  (key, value) => (key, value)
      }.toJson
    }


    println("Ready!")
    import Formatters._

    get("/active-jobs"){ (req: Request, res: Response) =>
      State.activeJobs.toJson
    }

    get("/cluster-cpu"){ (req: Request, res: Response) =>
      State.clusterCpu.toJson
    }

    get("/cluster-bandwidth"){ (req: Request, res: Response) =>
      State.clusterBandwidth.toJson
    }
  }
}
