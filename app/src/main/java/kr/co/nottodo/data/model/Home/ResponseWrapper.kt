package kr.co.nottodo.data.model.Home

data class ResponseWrapper<T>(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: T? = null
)
