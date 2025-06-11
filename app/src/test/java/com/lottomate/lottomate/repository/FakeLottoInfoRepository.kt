package com.lottomate.lottomate.repository

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LatestRoundInfo
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto645Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto720Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class FakeLottoInfoRepository : LottoInfoRepository {
    private val _allLatestLottoInfo = MutableStateFlow<Map<Int, LottoInfo>>(emptyMap())
    private val _latestLottoRoundInfo = MutableStateFlow<Map<Int, LatestRoundInfo>>(emptyMap())
    override val latestLottoRoundInfo: StateFlow<Map<Int, LatestRoundInfo>>
        get() = _latestLottoRoundInfo.asStateFlow()
    override val allLatestLottoInfo: Flow<Map<Int, LottoInfo>>
        get() = _allLatestLottoInfo.asStateFlow()

    override suspend fun fetchAllLatestLottoInfo() {
    }

    override fun fetchLottoInfo(lottoType: Int, lottoRndNum: Int?): Flow<LottoInfo> = flow {
        val info = when (lottoType) {
            LottoType.L645.num -> {
                Lotto645Info(
                    lottoPrize = listOf(
                        "27_680_521_880",
                        "4_613_420_316",
                        "4_613_423_150",
                        "8_218_200_000",
                        "13_618_590_000"
                    ),
                    totalSalesPrice = "17_488_305_000",
                    lottoPrizePerPerson = listOf(
                        "1_384_026_094",
                        "50_145_873",
                        "1_451_675",
                        "50_000",
                        "5_000"
                    ),
                    lottoRound = 1175,
                    lottoDate = "2025-06-07",
                    lottoNum = listOf(3, 4, 6, 8, 32, 42),
                    lottoBonusNum = listOf(31),
                    lottoWinnerNum = listOf("20", "92", "3_178", "164_364", "2_723_718")
                )

            }
            else -> {
                Lotto720Info(
                    lottoRound = 266,
                    lottoDate = "2025-06-05",
                    lottoNum = listOf(4, 0, 1, 5, 1, 9, 2), // 조 + 6자리 번호
                    lottoBonusNum = listOf(5, 4, 9, 4, 1, 7), // 보너스 각 조 번호
                    lottoWinnerNum = listOf("0", "0", "39", "542", "5_848", "55_490", "575_796") // 1~7등
                )
            }
        }

        emit(info)
    }
}