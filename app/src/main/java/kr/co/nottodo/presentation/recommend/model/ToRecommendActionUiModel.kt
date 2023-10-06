package kr.co.nottodo.presentation.recommend.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToRecommendActionUiModel(
    val id: Int,
    val title: String,
    val situation: String,
    val image: String,
) : Parcelable
