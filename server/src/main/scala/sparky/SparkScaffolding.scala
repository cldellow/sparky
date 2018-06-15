package sparky
import spark.{Request, Response, Route}

trait SparkScaffolding {
  def get(path: String)(f: (Request, Response) => Object): Unit = {
    spark.Spark.get(path, convertToRequest(path, f))
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
