package kr.co.nottodo.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestTokenDto(
    @SerialName("socialToken") val socialToken: String,
    @SerialName("fcmToken") val fcmToken: String,
)

@Serializable
data class ResponseTokenDto(
    @SerialName("status") val status: Int,
    @SerialName("success") val success: Boolean,
    @SerialName("message") val message: String,
    @SerialName("data") val data: AccessToken,
) {
    @Serializable
    data class AccessToken(
        @SerialName("accessToken") val accessToken: String,
    )
}