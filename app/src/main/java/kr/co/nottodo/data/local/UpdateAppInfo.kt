package kr.co.nottodo.data.local

import kotlinx.serialization.SerialName

data class UpdateAppInfo(
    @SerialName("app_version") val appVersion: Int,
    @SerialName("force_update") val appForceUpdate: Boolean,
)
