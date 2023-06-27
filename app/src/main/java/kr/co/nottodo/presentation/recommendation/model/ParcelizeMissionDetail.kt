package kr.co.nottodo.presentation.recommendation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelizeMissionDetail(
    val id: Int,
    val title: String,
    val situation: String,
) : Parcelable
