package sparky

case class Job(
  id: Int,
  description: String,
  duration: Int,
  tasksDone: Int,
  tasksActive: Int,
  tasksTotal: Int
)
