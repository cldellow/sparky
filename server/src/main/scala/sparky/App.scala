package sparky

import spark.Spark._
import spark.{Request, Response, Route}
import spray.json._
import DefaultJsonProtocol._

object App {
  def main(args: Array[String]): Unit = {
    println("ok")

    doGet("/hello"){ (req: Request, res: Response) => Map("a" -> 1, "b" -> 2).toJson }
  }

  def doGet(path: String)(f: (Request, Response) => Object): Unit = {
    get(path, convertToRequest(path, f))
  }

  // nicked from https://github.com/dnvriend/spark-scala/blob/master/src/main/scala/com/example/Spark.scala
  def convertToRequest(path: String, f: (Request, Response) ⇒ AnyRef): Route = {
    new Route() {
      override def handle(request: Request, response: Response): AnyRef = {
        f(request, response)
      }
    }
  }
}
