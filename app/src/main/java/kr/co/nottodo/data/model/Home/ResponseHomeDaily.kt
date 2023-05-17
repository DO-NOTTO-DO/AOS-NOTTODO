package kr.co.nottodo.data.model.Home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeDailyResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: List<HomeDaily>
) {
    @Serializable
    data class HomeDaily(
        @SerialName("id") val id: Long,
        @SerialName("title") val title: String,
        @SerialName("completionStatus") val completionStatus: String,
        @SerialName("situationName") val situationName: String
    )
}
