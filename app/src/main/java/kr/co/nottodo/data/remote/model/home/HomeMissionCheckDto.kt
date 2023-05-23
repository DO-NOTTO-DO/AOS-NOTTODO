package kr.co.nottodo.data.model.Home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestHomeMissionCheck(val completionStatus: String)

@Serializable
data class ResponseHomeMissionCheckDto(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: HomeMissionCheckDto
) {
    @Serializable
    data class HomeMissionCheckDto(
        @SerialName("completionStatus") val completionStatus: String,
        @SerialName("id") val id: Long,
        @SerialName("title") val title: String,
        @SerialName("situationName") val situationName: String,

        )
}
