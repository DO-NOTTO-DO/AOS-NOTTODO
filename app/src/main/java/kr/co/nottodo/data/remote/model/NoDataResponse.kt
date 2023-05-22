package kr.co.nottodo.data.remote.model

@kotlinx.serialization.Serializable
data class NoDataResponse(
    val message: String,
    val status: Int,
    val success: Boolean
)
