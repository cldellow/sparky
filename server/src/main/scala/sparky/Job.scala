package sparky

case class Job(
  id: Long,
  startMs: Long,
  finishMs: Long,
  description: String,
  duration: Int,
  tasksDone: Int,
  tasksActive: Int,
  tasksTotal: Int
)
