package kr.co.nottodo.data.remote.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseHomeWeekly(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: List<HomeMissionPercent>
) {

    @Serializable
    data class HomeMissionPercent(
        @SerialName("actionDate") val actionDate: String,
        @SerialName("percentage") val percentage: Float,
    )
}
