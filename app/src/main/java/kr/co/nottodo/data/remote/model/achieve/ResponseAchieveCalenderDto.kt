package kr.co.nottodo.data.remote.model.achieve

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseAchieveCalenderDto(
    val data: List<Data>,
    val message: String,
    val status: Int,
    val success: Boolean
) {
    @Serializable
    data class Data(
        @SerialName("actionDate") val actionDate: String,
        @SerialName("percentage") val percentage: Float
    )
}
