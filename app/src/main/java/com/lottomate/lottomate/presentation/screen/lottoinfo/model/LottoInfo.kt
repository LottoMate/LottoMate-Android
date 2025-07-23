package com.lottomate.lottomate.presentation.screen.lottoinfo.model

import com.lottomate.lottomate.domain.model.Lotto645ResultInfo
import com.lottomate.lottomate.domain.model.Lotto720ResultInfo

sealed interface LottoInfo {
    val lottoRound: Int?
    val lottoDate: String?
}

sealed interface LottoInfoWithBalls : LottoInfo {
    val lottoNum: List<Int>
    val lottoBonusNum: List<Int>
    val lottoWinnerNum: List<String>
    override val lottoRound: Int
    override val lottoDate: String
}

data class Lotto645Info(
    val lottoPrize: List<String>,
    val totalSalesPrice: String,
    val lottoPrizePerPerson: List<String>,
    override val lottoRound: Int,
    override val lottoDate: String,
    override val lottoNum: List<Int>,
    override val lottoBonusNum: List<Int>,
    override val lottoWinnerNum: List<String>,
): LottoInfoWithBalls {
    fun getWinningInfo() = Lotto645ResultInfo(
        firstPrize = lottoPrizePerPerson[0],
        secondPrize = lottoPrizePerPerson[1],
        thirdPrize = lottoPrizePerPerson[2],
        fourthPrize = lottoPrizePerPerson[3],
        fifthPrize = lottoPrizePerPerson[4],
    )
}

data class Lotto720Info(
    override val lottoRound: Int,
    override val lottoDate: String,
    override val lottoNum: List<Int>,
    override val lottoBonusNum: List<Int>,
    override val lottoWinnerNum: List<String>
): LottoInfoWithBalls {
    fun getWinningInfo() = Lotto720ResultInfo(
        firstPrize = lottoWinnerNum[0],
        secondPrize = lottoWinnerNum[1],
        thirdPrize = lottoWinnerNum[2],
        fourthPrize = lottoWinnerNum[3],
        fifthPrize = lottoWinnerNum[4],
        sixthPrize = lottoWinnerNum[5],
        seventhPrize = lottoWinnerNum[6],
    )
}

data class SpeettoInfo(
    val currentPage: Int,
    val lastPage: Int,
    val details: List<SpeettoInfoDetail>,
    override val lottoRound: Int? = null,
    override val lottoDate: String? = null,
) : LottoInfo

data class SpeettoInfoDetail(
    val rank: Int,
    val store: String,
    val hasInterview: Boolean,
    override val lottoRound: Int,
    override val lottoDate: String,
): LottoInfo

val SpeettoMockDatas = SpeettoInfo(
    currentPage = 1,
    lastPage = 69,
    details = listOf(
        SpeettoInfoDetail(
            rank = 1,
            lottoRound = 55,
            store = "진짜 이름이 아주 긴 판매점 보라매점보라매점",
            lottoDate = "2024.08.21",
            hasInterview = false
        ),
        SpeettoInfoDetail(
            rank = 1,
            lottoRound = 55,
            store = "진짜 이름이 아주 긴 판매점 보라매점",
            lottoDate = "2024.08.21",
            hasInterview = true
        ),
        SpeettoInfoDetail(
            rank = 1,
            lottoRound = 55,
            store = "CU알리앙스점",
            lottoDate = "2024.08.14",
            hasInterview = false
        ),
        SpeettoInfoDetail(
            rank = 1,
            lottoRound = 55,
            store = "CU알리앙스점",
            lottoDate = "2024.08.14",
            hasInterview = true
        ),
        SpeettoInfoDetail(
            rank = 1,
            lottoRound = 55,
            store = "신율편의마트",
            lottoDate = "2024.07.29",
            hasInterview = true
        ),
        SpeettoInfoDetail(
            rank = 1,
            lottoRound = 55,
            store = "신율편의마트",
            lottoDate = "2024.07.29",
            hasInterview = false
        ),
        SpeettoInfoDetail(
            rank = 2,
            lottoRound = 55,
            store = "GS25명장화목점",
            lottoDate = "2024.08.28",
            hasInterview = false
        ),
        SpeettoInfoDetail(
            rank = 2,
            lottoRound = 55,
            store = "공단로또명당",
            lottoDate = "2024.08.23",
            hasInterview = false
        ),
        SpeettoInfoDetail(
            rank = 2,
            lottoRound = 55,
            store = "행운복권마트",
            lottoDate = "2024.08.23",
            hasInterview = false
        ),
        SpeettoInfoDetail(
            rank = 2,
            lottoRound = 55,
            store = "남원도통복권방",
            lottoDate = "2024.08.14",
            hasInterview = false
        ),
    )
)