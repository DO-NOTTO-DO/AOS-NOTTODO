package kr.co.nottodo.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRecentMissionListDto(
    @SerialName("status") val status: Int,
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String,
    @SerialName("data") val data: List<Data>,
) {
    @Serializable
    data class Data(
        @SerialName("title") val title: String,
    )
}

@Serializable
data class ResponseRecommendSituationListDto(
    @SerialName("status") val status: Int,
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String,
    @SerialName("data") val data: List<RecommendSituation>,
) {
    @Serializable
    data class RecommendSituation(
        @SerialName("name") val name: String,
    )
}