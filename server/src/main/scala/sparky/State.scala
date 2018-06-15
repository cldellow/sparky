package sparky

object State {
  def activeJobs: List[Job] = {
    List(
      Job(123, "foo", 123, 12, 4, 20),
      Job(124, "bar", 234, 15, 4, 20)
    )
  }
}
