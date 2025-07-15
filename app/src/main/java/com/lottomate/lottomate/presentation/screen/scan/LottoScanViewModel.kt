package com.lottomate.lottomate.presentation.screen.scan

import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.scanResult.model.LotteryInputType
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLotto645Info
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLotto720Info
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLotto720InfoNumbers
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLottoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LottoScanViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
) : BaseViewModel(errorHandler) {
    fun parseMyLotto(data: String): Pair<LotteryInputType, MyLottoInfo> {
        return when {
            data.contains("pd") -> {
                LotteryInputType.ONLY720 to parseLotto720(data.substringAfter("pd"))
            }
            else -> {
                LotteryInputType.ONLY645 to parseLotto645(data)
            }
        }
    }

    /**
     * 실물 복권 용지(연금복권720)를 스캔한 데이터를 회차, 번호로 파싱합니다.
     *
     * @param data 실물 복권 용지를 스캔했을 때 나오는 데이터
     * @return MyLottoInfo 파싱된 데이터
     */
    private fun parseLotto720(data: String): MyLottoInfo {
        val (roundPart, numbers) = data.split("s")

        val round = roundPart.substring(3, 6)  // "256"
        val jo = roundPart.substring(6, 7)     // "1"

        return MyLottoInfo(
            myLotto720Info = MyLotto720Info(
                round = round.toInt(),
                numbers = listOf(
                    MyLotto720InfoNumbers(
                        firstNumber = jo.toInt(),
                        numbers = numbers.map { it.toString().toInt() },
                    )
                )
            )
        )
    }

    /**
     * 실물 복권 용지(로또645)를 스캔한 데이터를 회차, 번호로 파싱합니다.
     *
     * @param data 실물 복권 용지를 스캔했을 때 나오는 데이터
     * @return MyLottoInfo 파싱된 데이터
     */
    private fun parseLotto645(data: String): MyLottoInfo {
        val round = Regex("^\\d+").find(data)?.value

        val regex = "(q|n|m)(\\d+)".toRegex()
        val numbers = regex.findAll(data)
            .map { matchResult ->
                matchResult.groupValues[2].take(12)
            }.toList()

        return MyLottoInfo(
            myLotto645Info = MyLotto645Info(
                round = round?.toInt() ?: 0,
                numbers = numbers.map { raw -> raw.chunked(2).map { it.toInt() } },
            )
        )
    }
}