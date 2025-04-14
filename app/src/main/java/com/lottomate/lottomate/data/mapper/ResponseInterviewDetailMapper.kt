package com.lottomate.lottomate.data.mapper

import com.lottomate.lottomate.data.remote.response.interview.ResponseInterviewDetail
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewQnA
import com.lottomate.lottomate.presentation.screen.interview.model.InterviewUIModel

fun ResponseInterviewDetail.toUIModel(): InterviewUIModel {
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

    return InterviewUIModel(
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