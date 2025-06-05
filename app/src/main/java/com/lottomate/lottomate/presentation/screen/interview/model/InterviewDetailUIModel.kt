package com.lottomate.lottomate.presentation.screen.interview.model

import kotlinx.serialization.Serializable

@Serializable
data class InterviewDetailUiModel(
    val no: Int,
    val title: String,
    val lottoRound: Int = 0,
    val lottoPrize: Double = 0.0,
    val interviewDate: String,
    val uploadDate: String,
    val thumb: String,
    val imgs: List<String>,
    val contents: List<InterviewQnA>,
    val originalNo: Int,
)

@Serializable
data class InterviewQnA(
    val question: String,
    val answer: String,
)