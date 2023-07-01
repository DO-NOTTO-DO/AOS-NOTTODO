package kr.co.nottodo.presentation.recommendation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendMissionUiModel(
    val id: Int,
    val title: String,
    val situation: String,
    val image: String,
) : Parcelable
