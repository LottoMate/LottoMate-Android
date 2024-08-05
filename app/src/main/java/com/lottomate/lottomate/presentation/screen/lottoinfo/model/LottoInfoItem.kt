package com.lottomate.lottomate.presentation.screen.lottoinfo.model

data class LottoInfoItem(
    val roundNum: Int,
    val drwtDate: String,
    val drwtNums: List<Int>,
    val drwtBonusNum: Int,
) {
    companion object {
        val lottoInfoMock = listOf(
            LottoInfoItem(
                roundNum = 1125,
                drwtDate = "2024-06-22",
                drwtNums = listOf(6, 14, 25, 33, 40, 44),
                drwtBonusNum = 30,
            ),
            LottoInfoItem(
                roundNum = 1126,
                drwtDate = "2024-06-29",
                drwtNums = listOf(4, 5, 9, 11, 37, 40),
                drwtBonusNum = 7,
            ),
            LottoInfoItem(
                roundNum = 1127,
                drwtDate = "2024-07-06",
                drwtNums = listOf(10, 15, 24, 30, 31, 37),
                drwtBonusNum = 32,
            ),
            LottoInfoItem(
                roundNum = 1128,
                drwtDate = "2024-07-13",
                drwtNums = listOf(1, 5, 8, 16, 28, 33),
                drwtBonusNum = 45,
            ),
            LottoInfoItem(
                roundNum = 1129,
                drwtDate = "2024-07-20",
                drwtNums = listOf(5, 10, 11, 17, 28, 34),
                drwtBonusNum = 22,
            ),
            LottoInfoItem(
                roundNum = 1130,
                drwtDate = "2024-07-27",
                drwtNums = listOf(15, 19, 21, 25, 27, 28),
                drwtBonusNum = 40,
            ),
            LottoInfoItem(
                roundNum = 1131,
                drwtDate = "2024-08-03",
                drwtNums = listOf(1, 2, 6, 14, 27, 38),
                drwtBonusNum = 33,
            ),
        )
    }
}