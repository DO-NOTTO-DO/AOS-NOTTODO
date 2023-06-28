package kr.co.nottodo.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelizeBottomDetailRegister(
    val title: String?,
    val situation: String?,
    val actions: List<Action>?,
    val goal: String?,
) : Parcelable {
    @Parcelize
    data class Action(
        val name: String?,
    ) : Parcelable
}
