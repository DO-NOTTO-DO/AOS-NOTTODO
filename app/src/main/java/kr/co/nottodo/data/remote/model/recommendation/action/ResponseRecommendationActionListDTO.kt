package kr.co.nottodo.data.remote.model.recommendation.action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRecommendationActionListDTO(
    @SerialName("status") val status: Int,
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String,
    @SerialName("data") val data: List<Action>,
) {
    @Serializable
    data class Action(
        @SerialName("id") val id: Int,
        @SerialName("title") val title: String,
        @SerialName("recommendActions") val recommendActions: CategoryList,
    ) {
        @Serializable
        data class CategoryList(
            @SerialName("name") val name: String,
        )
    }
}
