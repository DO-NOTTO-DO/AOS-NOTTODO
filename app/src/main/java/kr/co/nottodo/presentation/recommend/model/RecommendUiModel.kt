package kr.co.nottodo.presentation.recommend.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendUiModel(
    val title: String,
    val situation: String,
    val actionList: List<String>,
) : Parcelable