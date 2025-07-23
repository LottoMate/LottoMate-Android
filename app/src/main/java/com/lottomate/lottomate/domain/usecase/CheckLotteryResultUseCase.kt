package com.lottomate.lottomate.domain.usecase

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.di.DispatcherModule
import com.lottomate.lottomate.domain.model.LotteryResult
import com.lottomate.lottomate.domain.model.LotteryResultInfo
import com.lottomate.lottomate.domain.model.LottoRank
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto645Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto720Info
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLotto645Info
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLotto720Info
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLottoInfo
import com.lottomate.lottomate.utils.DateUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckLotteryResultUseCase @Inject constructor(
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val lottoInfoRepository: LottoInfoRepository,
) {
    suspend operator fun invoke(type: LottoType, myLotto: MyLottoInfo): Result<LotteryResult> {
        return try {
            val result = when (type) {
                LottoType.L645 -> {
                    myLotto.myLotto645Info?.let { myLotto645Info ->
                        withContext(ioDispatcher) {
                            val lotto645Info = lottoInfoRepository.fetchLottoInfo(type.num, myLotto645Info.round).first()
                            checkLotto645Result(myLotto645Info, lotto645Info as Lotto645Info)
                        }
                    }
                }

                LottoType.L720 -> {
                    myLotto.myLotto720Info?.let { myLotto720Info ->
                        withContext(ioDispatcher) {
                            val lotto720Info = lottoInfoRepository.fetchLottoInfo(type.num, myLotto720Info.round).first()
                            checkLotto720Result(myLotto720Info, lotto720Info as Lotto720Info)
                        }
                    }
                }

                else -> throw IllegalArgumentException("Invalid lotto info")
            }

            result?.let {
                val winResults = it.copy(resultInfos = it.resultInfos.filter { it.isWinner })

                Result.success(winResults)
            } ?: Result.failure(IllegalArgumentException("Invalid lotto info"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun checkLotto645Result(
        myLotto: MyLotto645Info,
        lottoInfo: Lotto645Info
    ): LotteryResult {
        val winningInfo = lottoInfo.getWinningInfo()

        val results = myLotto.numbers.mapIndexed { index, myNumbers ->
            val winCount = myNumbers.count { lottoInfo.lottoNum.contains(it) }
            val bonus = myNumbers.contains(lottoInfo.lottoBonusNum.first())
            val rank = LottoRank.getLotto645Rank(winCount, bonus)

            LotteryResultInfo(
                lottoType = LottoType.L645,
                round = lottoInfo.lottoRound,
                isWinner = LottoRank.isWin(rank),
                rank = rank,
                winningNumbers = myNumbers,
                winningInfo = winningInfo,
                isClaimPeriodExpired = DateUtils.isPrizeExpired(lottoInfo.lottoDate.replace(".", "-")),
            )
        }

        return LotteryResult(
            isWinner = results.any { it.isWinner },
            resultInfos = results,
        )
    }

    private fun checkLotto720Result(
        myLotto: MyLotto720Info,
        lottoInfo: Lotto720Info
    ): LotteryResult {
        val lottoFirstNumber = lottoInfo.lottoNum.first()
        val winningInfo = lottoInfo.getWinningInfo()

        val results = myLotto.numbers.map { (numbers, firstNumber) ->
            val isSameFirstNumber = lottoFirstNumber == firstNumber
            val lottoNumbersWithoutFirst = lottoInfo.lottoNum.drop(1)

            val matchCount = numbers.withIndex().count { (index, number) ->
                lottoNumbersWithoutFirst[index] == number
            }.plus(if (isSameFirstNumber) 1 else 0)

            val isAllBonusNumbersMatched = isMatchBonus(numbers, lottoInfo.lottoBonusNum)
            val rank = LottoRank.getLotto720Rank(
                count = if (isAllBonusNumbersMatched) L720_BONUS_WIN_COUNT else matchCount,
                isbonus = isAllBonusNumbersMatched,
            )

            LotteryResultInfo(
                lottoType = LottoType.L720,
                round = lottoInfo.lottoRound,
                isWinner = LottoRank.isWin(rank),
                rank = rank,
                winningNumbers = listOf(firstNumber) + numbers,
                winningInfo = winningInfo,
                isClaimPeriodExpired = DateUtils.isPrizeExpired(lottoInfo.lottoDate.replace(".", "-")),
            )
        }

        return LotteryResult(
            isWinner = results.any { it.isWinner },
            resultInfos = results,
        )
    }

    private fun isMatchBonus(numbers: List<Int>, bonusNumbers: List<Int>): Boolean =
        numbers.withIndex().count { (index, number) -> bonusNumbers[index] == number } == L720_BONUS_WIN_COUNT


    companion object {
        private const val L720_BONUS_WIN_COUNT = 6
    }
}