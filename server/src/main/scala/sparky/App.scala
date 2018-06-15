package sparky

import spark.{Request, Response}
import spray.json._
import DefaultJsonProtocol._

object App extends SparkScaffolding {
  def main(args: Array[String]): Unit = {
    println("ok")

    get("/hello"){ (req: Request, res: Response) => Map("a" -> 1, "b" -> 2).toJson }
  }
}
