package kr.co.nottodo.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestAdditionDto(
    @SerialName("title") val title: String,
    @SerialName("situation") val situation: String,
    @SerialName("actions") val actions: List<String>?,
    @SerialName("goal") val goal: String?,
    @SerialName("dates") val dates: List<String>,
)

@Serializable
data class ResponseAdditionDto(
    @SerialName("status") val status: Int,
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String,
    @SerialName("data") val data: Addition,
) {
    @Serializable
    data class Addition(
        @SerialName("title") val title: String,
        @SerialName("situation") val situation: String,
        @SerialName("actions") val actions: List<ResponseModificationDto.Modification.Action>?,
        @SerialName("goal") val goal: String?,
        @SerialName("dates") val dates: List<String>,
    )
}