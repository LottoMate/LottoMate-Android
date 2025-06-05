package com.lottomate.lottomate.data.mapper

import com.lottomate.lottomate.R
import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviewDetail
import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviewsInfo
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUiModel
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewQnA
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewDetailUiModel

fun ResponseInterviewDetail.toUIModel(): InterviewDetailUiModel {
    val contents = reviewCont
        .split("▶")
        .filter { it.isNotBlank() }

    val qna = contents.map {
        val question = it.substringBefore("?").trim() + "?"
        val answer = it.substringAfter("?")
            .trimStart()
            .removePrefix("->")
            .trim()
        InterviewQnA(question, answer)
    }

    return InterviewDetailUiModel(
        no = reviewNo,
        originalNo = reviewHref,
        title = reviewTitle,
        interviewDate = intrvDate.replace("-", "."),
        uploadDate = reviewDate.replace("-", "."),
        thumb = reviewThumb,
        imgs = reviewImg,
        contents = qna.toList(),
    )
}

fun ResponseInterviewsInfo.toUiModel() = InterviewUiModel(
    no = this.reviewNo,
    title = this.reviewTitle,
    thumbs = this.reviewThumb,
    emptyThumbs = randomEmptyThumbnailId(),
    date = this.intrvDate.replace("-", "."),
    place = this.reviewPlace,
)

fun randomEmptyThumbnailId(): Int {
    val random = (0..1).random()

    return when (random) {
        0 -> R.drawable.img_interview_empty01
        else -> R.drawable.img_interview_empty02
    }
}