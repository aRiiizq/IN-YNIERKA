package Data

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val c_password: String
)