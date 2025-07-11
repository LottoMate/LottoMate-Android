package com.lottomate.lottomate.domain.usecase

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.model.LotteryResult
import com.lottomate.lottomate.domain.model.Lotto645ResultInfo
import com.lottomate.lottomate.domain.model.Lotto720ResultInfo
import com.lottomate.lottomate.domain.model.LottoRank
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto645Info
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.Lotto720Info
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLotto645Info
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLotto720Info
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLottoInfo
import com.lottomate.lottomate.utils.DateUtils
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckLotteryResultUseCase @Inject constructor(
    private val lottoInfoRepository: LottoInfoRepository,
) {
    suspend operator fun invoke(type: LottoType, myLotto: MyLottoInfo): Result<LotteryResult> {
        val lottoInfo = lottoInfoRepository.fetchLottoInfo(type.num, myLotto.round).first()

        return try {
            val result = when (type) {
                LottoType.L645 -> {
                    val lotto645Info = lottoInfo as Lotto645Info
                    val myLotto645 = myLotto as MyLotto645Info

                    checkLotto645Result(myLotto645, lotto645Info)
                }
                else -> {
                    val lotto720Info = lottoInfo as Lotto720Info
                    val myLotto720 = myLotto as MyLotto720Info

                    checkLotto720Result(myLotto720, lotto720Info)
                }
            }

            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun checkLotto645Result(myLotto: MyLotto645Info, lottoInfo: Lotto645Info): LotteryResult {
        val winningRanks = myLotto.numbers.mapIndexed { index, myNumbers ->
            val winCount = myNumbers.count { lottoInfo.lottoNum.contains(it) }
            val bonus = myNumbers.contains(lottoInfo.lottoBonusNum.first())

            index to LottoRank.getLotto645Rank(winCount, bonus)
        }

        val winningNumbers = winningRanks
            .filterNot { it.second == LottoRank.NONE }
            .map { myLotto.numbers[it.first] }

        return LotteryResult(
            lottoType = LottoType.L645,
            isWinner = winningRanks.any { it.second != LottoRank.NONE },
            winningRanks = winningRanks.map { it.second },
            winningNumbers = winningNumbers,
            winningInfo = Lotto645ResultInfo(
                firstPrize = lottoInfo.lottoPrizePerPerson[0],
                secondPrize = lottoInfo.lottoPrizePerPerson[1],
                thirdPrize = lottoInfo.lottoPrizePerPerson[2],
                fourthPrize = lottoInfo.lottoPrizePerPerson[3],
                fifthPrize = lottoInfo.lottoPrizePerPerson[4],
            ),
            isClaimPeriodExpired = DateUtils.isPrizeExpired(lottoInfo.lottoDate.replace(".", "-")),
        )
    }

    private fun checkLotto720Result(myLotto: MyLotto720Info, lottoInfo: Lotto720Info): LotteryResult {
        val winFirstNum = lottoInfo.lottoNum.first() == myLotto.firstNumber
        val lottoNumWithoutFirst = lottoInfo.lottoNum.drop(1)

        val winCount = myLotto.numbers.withIndex().count { (index, number) ->
            lottoNumWithoutFirst[index] == number
        }.plus(if (winFirstNum) 1 else 0)

        val isWinningBonus = myLotto.numbers.all { number ->
            lottoInfo.lottoBonusNum.contains(number)
        }

        val winningRank = LottoRank.getLotto720Rank(
            count = if (isWinningBonus) L720_BONUS_WIN_COUNT else winCount,
            isbonus = isWinningBonus,
        )

        return LotteryResult(
            lottoType = LottoType.L720,
            isWinner = winningRank != LottoRank.NONE,
            winningRanks = listOf(winningRank),
            winningNumbers = listOf(if (winningRank != LottoRank.NONE) listOf(myLotto.firstNumber) + myLotto.numbers else emptyList()),
            winningInfo = Lotto720ResultInfo(
                firstPrize = lottoInfo.lottoWinnerNum[0],
                secondPrize = lottoInfo.lottoWinnerNum[1],
                thirdPrize = lottoInfo.lottoWinnerNum[2],
                fourthPrize = lottoInfo.lottoWinnerNum[3],
                fifthPrize = lottoInfo.lottoWinnerNum[4],
                sixthPrize = lottoInfo.lottoWinnerNum[5],
                seventhPrize = lottoInfo.lottoWinnerNum[6],
            ),
            isClaimPeriodExpired = DateUtils.isPrizeExpired(lottoInfo.lottoDate.replace(".", "-")),
        )
    }

    companion object {
        private const val L720_BONUS_WIN_COUNT = 6
    }
}