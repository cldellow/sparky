package sparky

import spark.{Request, Response}
import spray.json._
import DefaultJsonProtocol._

object Formatters extends DefaultJsonProtocol {
  implicit val jobFormatter = jsonFormat6(Job)
  implicit val tsValueFormatter = jsonFormat2(TsValue)
  implicit val clusterBandwidth = jsonFormat4(Bandwidth)
}


object App extends SparkScaffolding {
  def main(args: Array[String]): Unit = {
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
