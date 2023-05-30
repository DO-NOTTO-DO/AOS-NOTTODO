

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class RecommendationMainListDTO(
    @SerialName("status")
    val status: Int,
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<MainList>
) {


    @Serializable
    data class MainList(
        @SerialName("id")
        val id: Int,
        @SerialName("title")
        val title: String,
        @SerialName("situation")
        val situation: String,
        @SerialName("description")
        val description: String,
        @SerialName("image")
        val image: String,

        )
}


