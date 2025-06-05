package com.lottomate.lottomate.presentation.screen.interview.model

import androidx.annotation.DrawableRes

data class InterviewUiModel(
    val no: Int,
    val title: String,
    val thumbs: String,
    @DrawableRes val emptyThumbs: Int,
    val date: String,
    val place: String,
)