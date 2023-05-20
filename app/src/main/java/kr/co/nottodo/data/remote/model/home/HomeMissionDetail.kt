package kr.co.nottodo.data.remote.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponHomeMissionDetail(
    val status: Int,
    val success: Boolean,
    val message: String,
    val data: HomeMissionDetail
) {

    @Serializable
    data class HomeMissionDetail(
        @SerialName("id") val id: Long,
        @SerialName("title") val title: String,
        @SerialName("situation") val situation: String,
        @SerialName("actions") val actions: List<Action>?,
        @SerialName("count") val count: Int,
        @SerialName("goal") val goal: String?,
    )

    @Serializable
    data class Action(
        val name: String?
    )
}
