package com.lottomate.lottomate.presentation.screen.pocket.model

import com.lottomate.lottomate.data.model.LottoType

/**
 * 내 로또 내역에서 사용되는 로또 Model
 *
 * round 로또 회차
 * date 로또 당첨 날짜
 * type 로또 종류 (로또645, 연금복권720, 스피또)
 * numbers 로또 숫자
 * condition 당첨 확인 전/후, 당첨 결과
 */
data class LottoDetail(
    val round: Int,
    val date: String,
    val type: LottoType,
    val numbers: List<Int>,
    val condition: LottoCondition = LottoCondition.NOT_CHECKED,
)

enum class LottoCondition {
    NOT_CHECKED,    // 결과 확인 전
    NOT_CHECKED_END,    // 결과 확인 전 (발표일 1년 초과)
    NOT_WON,        // 결과 확인 완료(미당첨)
    CHECKED_WIN     // 결과 확인 완료(당첨)
}

val mockLottoDetails = listOf(
    LottoDetail(
        round = 1010,
        date = "2024.12.16",
        type = LottoType.L645,
        numbers = listOf(1, 5, 10, 23, 35, 42),
        condition = LottoCondition.NOT_CHECKED
    ),
    LottoDetail(
        round = 1008,
        date = "2024.12.02",
        type = LottoType.L645,
        numbers = listOf(3, 7, 11, 15, 28, 36),
        condition = LottoCondition.CHECKED_WIN
    ),
    LottoDetail(
        round = 1008,
        date = "2024.12.02",
        type = LottoType.L720,
        numbers = listOf(4, 2, 8, 5, 4, 5, 7),
        condition = LottoCondition.NOT_WON
    ),
    LottoDetail(
        round = 1008,
        date = "2024.12.02",
        type = LottoType.L645,
        numbers = listOf(6, 9, 14, 19, 27, 38),
        condition = LottoCondition.CHECKED_WIN
    ),
    LottoDetail(
        round = 1006,
        date = "2024.11.18",
        type = LottoType.L720,
        numbers = listOf(4, 1, 8, 2, 0, 1, 7),
        condition = LottoCondition.NOT_CHECKED_END
    )
)
