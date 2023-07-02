package kr.co.nottodo.presentation.recommendation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendMissionActionUiModel(
    val title: String,
    val situation: String,
    val actionList: List<String>,
) : Parcelable