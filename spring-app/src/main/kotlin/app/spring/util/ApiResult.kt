package app.spring.util

data class ApiResult<T>(
    val state : ApiState = ApiState.OK,
    val message: String? = null,
    val data: T? = null,
)

enum class ApiState {
    OK,
    ERROR;
}