package Models

// Data classes for different models
data class CoachSchedule(
    val day: String,
    val workHours: List<WorkHour>
)

data class WorkHour(
    val startTime: String,
    val endTime: String,
    val isAvailable: Boolean,
    val client: Client? = null
)

data class Client(
    val id: String,
    val name: String,
    val email: String
)

data class ExercisePlan(
    val exercises: List<Exercise>
)

data class Exercise(
    val name: String,
    val description: String
)

data class SessionRequest(
    val client: Client,
    val workHour: WorkHour,
    val status: String // e.g., PENDING, CONFIRMED, DECLINED
)
