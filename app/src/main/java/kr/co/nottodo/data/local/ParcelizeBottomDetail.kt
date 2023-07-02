package kr.co.nottodo.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelizeBottomDetail(
    val id: Long,
    val title: String,
    val situation: String,
    val actions: List<Action>?,
    val count: Int,
    val goal: String?,
    val date: String
) : Parcelable {
    @Parcelize
    data class Action(
        val name: String?,
    ) : Parcelable
}
