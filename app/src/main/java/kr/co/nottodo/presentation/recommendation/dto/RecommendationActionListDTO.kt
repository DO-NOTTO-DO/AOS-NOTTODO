import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationActionListDTO(
    @SerialName("status")
    val status: Int,
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: List<ActionList>,
) {
    @Serializable
    data class ActionList(
        @SerialName("id")
        val id: Int,
        @SerialName("title")
        val title: String,
        @SerialName("recommendActions")
        val recommendActions: List<CategoryList>
    ) {
        @Serializable
        data class CategoryList(
            @SerialName("name")
            val name: String
        )
    }
}
