package kr.co.nottodo.data.model.Home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeDailyResponse(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: List<HomeDaily>
) {
    @Serializable
    data class HomeDaily(
        @SerialName("id") val id: Long,
        @SerialName("title") val title: String,
        @SerialName("completionStatus") val completionStatus: String,
        @SerialName("situationName") val situationName: String
    ) {
        val isChecked: Boolean
            get() {
                return when (completionStatus) {
                    Checked.CHECKED.text -> Checked.CHECKED.value
                    Checked.UNCHECKED.text -> Checked.UNCHECKED.value
                    else -> throw IllegalArgumentException()
                }
            }

        enum class Checked(val text: String, val value: Boolean) {
            CHECKED("CHECKED", true),
            UNCHECKED("UNCHECKED", false);

            companion object {
                fun reverseCheck(isChecked: Boolean): String {
                    return when (isChecked) {
                        CHECKED.value -> UNCHECKED.text
                        UNCHECKED.value -> CHECKED.text
                        else -> throw IllegalArgumentException()
                    }
                }
            }
        }
    }
}
