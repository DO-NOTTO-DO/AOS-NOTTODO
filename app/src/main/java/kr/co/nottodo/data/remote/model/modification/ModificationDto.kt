package kr.co.nottodo.data.remote.model.modification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestModificationDto(
    @SerialName("title") val title: String,
    @SerialName("situation") val situation: String,
    @SerialName("actions") val actions: List<String>?,
    @SerialName("goal") val goal: String?,
)

@Serializable
data class ResponseModificationDto(
    @SerialName("status") val status: Int,
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String,
    @SerialName("data") val data: Modification,
) {
    @Serializable
    data class Modification(
        @SerialName("title") val title: String,
        @SerialName("situation") val situation: String,
        @SerialName("actions") val actions: List<Action>?,
        @SerialName("goal") val goal: String?,
    ) {
        @Serializable
        data class Action(
            @SerialName("name")
            val name: String,
        )
    }
}

@Serializable
data class ResponseGetMissionDates(
    @SerialName("status")
    val status: Int,
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<String>,
)