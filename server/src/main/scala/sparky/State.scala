package sparky

import scala.util.Random

object State {
  def activeJobs: Seq[Job] = {
    Seq(
      Job(123, "foo", 123, 12, 4, 20),
      Job(124, "bar", 234, 15, 4, 20)
    )
  }

  def clusterCpu: Seq[TsValue] = {
    // Use a seed so the numbers are stable
    val r = new Random(0)
    val now = System.currentTimeMillis / 1000
    // Throw away a consistent number of values to prevent jitter
    (0 to (now.toInt % 1000)).foreach { x => r.nextDouble }

    (now - 120 to now).map { sec =>
      TsValue(sec, r.nextDouble)
    }
  }
}
