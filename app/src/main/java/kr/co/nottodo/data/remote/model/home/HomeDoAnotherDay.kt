package kr.co.nottodo.data.model.Home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestHomeDoAnotherDay(val dates: List<String>)

@Serializable
data class ResponseHomeDoAnotherDay(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: HomeDoAnotherDayDto?
) {
    @Serializable
    data class HomeDoAnotherDayDto(
        @SerialName("dates") val dates: List<String>,
    )
}
