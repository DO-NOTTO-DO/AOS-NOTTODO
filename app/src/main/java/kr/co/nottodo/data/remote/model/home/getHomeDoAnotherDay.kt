package kr.co.nottodo.data.model.Home

import kotlinx.serialization.Serializable

@Serializable
data class getResponseHomeDoAnotherDay(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: List<String>,
)
