package com.lottomate.lottomate.presentation.screen.interview.model

import kotlinx.serialization.Serializable

@Serializable
data class Interview(
    val title: String,
    val subTitle: String,
    val lottoRound: Int,
    val lottoPrize: Double,
    val interviewDate: String,
    val uploadDate: String,
    val link: String,
    val thumb: String,
    val imgs: List<String>,
    val contents: List<InterviewQnA>,
)

@Serializable
data class InterviewQnA(
    val question: String,
    val answer: String,
)

val InterviewMockData = Interview(
    title = "사회 초년생 시절부터 꾸준히 구매해서 1등 당첨!",
    subTitle = "스피또2000 1등",
    lottoRound = 55,
    lottoPrize = 20.0,
    interviewDate = "2024.08.21",
    uploadDate = "2024.08.21",
    link = "https://dhlottery.co.kr/gameResult.do?method=highWinView",
    thumb = "",
    imgs = List(3) { "https://placehold.co/600x400.png" },
    contents = listOf(
        InterviewQnA(
            question = "당첨되신 걸 어떻게 알게 되셨고, 또 알았을 때 기분이 어떠셨나요?",
            answer = "복권 당첨 후기 이벤트 멘트 작성 예정입니다. 질문에 대한 답변입니다. 나도 당첨되고 싶다. 저희 모두 당첨되게 해주세요. 신축 빌라 갖고 싶당."
        ),
        InterviewQnA(
            question = "당첨되신 걸 어떻게 알게 되셨고, 또 알았을 때 기분이 어떠셨나요?",
            answer = "복권 당첨 후기 이벤트 멘트 작성 예정입니다. 질문에 대한 답변입니다. 나도 당첨되고 싶다. 저희 모두 당첨되게 해주세요. 신축 빌라 갖고 싶당."
        ),
        InterviewQnA(
            question = "당첨되신 걸 어떻게 알게 되셨고, 또 알았을 때 기분이 어떠셨나요?",
            answer = "복권 당첨 후기 이벤트 멘트 작성 예정입니다. 질문에 대한 답변입니다. 나도 당첨되고 싶다. 저희 모두 당첨되게 해주세요. 신축 빌라 갖고 싶당."
        ),
        InterviewQnA(
            question = "당첨되신 걸 어떻게 알게 되셨고, 또 알았을 때 기분이 어떠셨나요?",
            answer = "복권 당첨 후기 이벤트 멘트 작성 예정입니다. 질문에 대한 답변입니다. 나도 당첨되고 싶다. 저희 모두 당첨되게 해주세요. 신축 빌라 갖고 싶당."
        ),
        InterviewQnA(
            question = "당첨되신 걸 어떻게 알게 되셨고, 또 알았을 때 기분이 어떠셨나요?",
            answer = "복권 당첨 후기 이벤트 멘트 작성 예정입니다. 질문에 대한 답변입니다. 나도 당첨되고 싶다. 저희 모두 당첨되게 해주세요. 신축 빌라 갖고 싶당."
        ),
    )
)