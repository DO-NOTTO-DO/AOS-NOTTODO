package kr.co.nottodo.data.remote.model.recommendation.mission

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ResponseRecommendMissionListDto(
    @SerialName("status") val status: Int,
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String,
    @SerialName("data") val data: List<Mission>,
) {

    @Serializable
    data class Mission(
        @SerialName("id") val id: Int,
        @SerialName("title") val title: String,
        @SerialName("situation") val situation: String,
        @SerialName("description") val description: String,
        @SerialName("image") val image: String,

        )
}


