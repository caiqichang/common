package app.spring.util

/**
 * 通用接口响应参数
 */
data class ApiResult<T>(
    val state : ApiState = ApiState.OK,
    val message: String? = null,
    val data: T? = null,
)

enum class ApiState {
    OK,
    ERROR;
}