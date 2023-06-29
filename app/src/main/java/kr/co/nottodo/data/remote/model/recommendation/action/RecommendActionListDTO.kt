package kr.co.nottodo.data.remote.model.recommendation.action

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRecommendActionListDTO(
    @SerialName("status") val status: Int,
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String,
    @SerialName("data") val data: Mission,
) {
    @Serializable
    data class Mission(
        @SerialName("id") val id: Int,
        @SerialName("title") val title: String,
        @SerialName("recommendActions") val recommendActions: List<Action>,
    ) {
        @Serializable
        data class Action(
            @SerialName("name") val name: String,
            @SerialName("description") val description: String?,
        )
    }
}
