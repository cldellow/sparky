package sparky

import spark.{Request, Response}
import spray.json._
import DefaultJsonProtocol._

object Formatters extends DefaultJsonProtocol {
  implicit val jobFormatter = jsonFormat6(Job)
}


object App extends SparkScaffolding {
  def main(args: Array[String]): Unit = {
    println("Ready!")
    import Formatters._

    get("/active-jobs"){ (req: Request, res: Response) =>
      State.activeJobs.toJson
    }
  }
}
