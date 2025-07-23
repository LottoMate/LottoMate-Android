package com.lottomate.lottomate.usecase

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.domain.model.LottoRank
import com.lottomate.lottomate.domain.usecase.CheckLotteryResultUseCase
import com.lottomate.lottomate.presentation.screen.result.model.MyLotto645Info
import com.lottomate.lottomate.presentation.screen.result.model.MyLotto720Info
import com.lottomate.lottomate.repository.FakeLottoInfoRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CheckLotteryResultUseCaseTest {
    private lateinit var useCase: CheckLotteryResultUseCase

    @Before
    fun setUp() {
        useCase = CheckLotteryResultUseCase(FakeLottoInfoRepository())
    }

    @Test
    fun `모두 미당첨된 로또645일 경우, 당첨 결과의 isWinner는 false이다`() = runTest {
        val lotto645 = listOf(
            listOf(1, 2, 5, 7, 9, 11),
            listOf(13, 15, 18, 22, 24, 25),
            listOf(12, 14, 16, 18, 20, 30),
            listOf(9, 19, 29, 39, 41, 43),
            listOf(7, 14, 22, 25, 28, 38),
        )

        val result = useCase(LottoType.L645, myLotto645.copy(numbers = lotto645)).getOrThrow()

        Assert.assertFalse(result.isWinner)
    }

    @Test
    fun `모두 당첨된 로또645일 경우, 당첨 결과의 isWinner는 true이다`() = runTest {
        val lotto645 = listOf(
            listOf(3, 4, 6, 8, 32, 42), // 1등
            listOf(3, 4, 6, 8, 32, 31), // 2등 (보너스 매치)
            listOf(3, 4, 6, 8, 32, 10), // 3등
            listOf(3, 4, 6, 8, 22, 23), // 4등
            listOf(3, 4, 6, 20, 21, 22), // 5등
        )

        val result = useCase(LottoType.L645, myLotto645.copy(numbers = lotto645)).getOrThrow()

        Assert.assertTrue(result.isWinner)
    }

    @Test
    fun `1줄 이상 당첨된 로또645일 경우, 당첨 결과의 isWinner는 true이다`() = runTest {
        val lotto645 = listOf(
            listOf(3, 4, 6, 8, 10, 11), // 4개 = 4등
            listOf(3, 4, 6, 31, 11, 12), // 3개 + 보너스 = 5등
            listOf(3, 4, 22, 23, 24, 25), // 2개 = 미당첨
            listOf(3, 6, 8, 10, 11, 12), // 3개 = 5등
            listOf(4, 6, 32, 11, 13, 14), // 3개 = 5등
        )

        val result = useCase(LottoType.L645, myLotto645.copy(numbers = lotto645)).getOrThrow()

        Assert.assertTrue(result.isWinner)
    }

    @Test
    fun `1등에 당첨된 로또645일 경우, winningRanks에 FIRST가 포함되어야 한다`() = runTest {
        val lotto645 = listOf(
            listOf(3, 4, 6, 8, 32, 42), // 1등
        )

        val result = useCase(LottoType.L645, myLotto645.copy(numbers = lotto645)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.L645_FIRST))
    }

    @Test
    fun `2등에 당첨된 로또645일 경우, winningRanks에 SECOND가 포함되어야 한다`() = runTest {
        val lotto645 = listOf(
            listOf(3, 4, 6, 8, 32, 31), // 2등
        )

        val result = useCase(LottoType.L645, myLotto645.copy(numbers = lotto645)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.L645_SECOND))
    }

    @Test
    fun `3등에 당첨된 로또645일 경우, winningRanks에 THIRD가 포함되어야 한다`() = runTest {
        val lotto645 = listOf(
            listOf(3, 4, 6, 8, 32, 10), // 3등
        )

        val result = useCase(LottoType.L645, myLotto645.copy(numbers = lotto645)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.L645_THIRD))
    }

    @Test
    fun `4등에 당첨된 로또645일 경우, winningRanks에 FOURTH가 포함되어야 한다`() = runTest {
        val lotto645 = listOf(
            listOf(3, 4, 6, 8, 10, 11), // 4등
        )

        val result = useCase(LottoType.L645, myLotto645.copy(numbers = lotto645)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.L645_FOURTH))
    }

    @Test
    fun `5등에 당첨된 로또645일 경우, winningRanks에 FIFTH가 포함되어야 한다`() = runTest {
        val lotto645 = listOf(
            listOf(4, 6, 32, 11, 13, 14), // 5등
        )

        val result = useCase(LottoType.L645, myLotto645.copy(numbers = lotto645)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.L645_FIFTH))
    }

    @Test
    fun `미당첨된 로또645일 경우, winningRanks에 NONE가 포함되어야 한다`() = runTest {
        val lotto645 = listOf(
            listOf(7, 14, 22, 25, 28, 38),  // 미당첨
        )

        val result = useCase(LottoType.L645, myLotto645.copy(numbers = lotto645)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.NONE))
    }

    @Test
    fun `당첨된 로또645일 경우, winningNumbers에 당첨된 번호가 존재한다`() = runTest {
        val lotto645 = listOf(
            listOf(4, 6, 32, 11, 13, 14), // 5등
        )

        val result = useCase(LottoType.L645, myLotto645.copy(numbers = lotto645)).getOrThrow()

        Assert.assertEquals(lotto645, result.winningNumbers)
    }

    @Test
    fun `미당첨된 로또645일 경우, winningNumbers에 당첨된 번호가 존재하지 않는다`() = runTest {
        val lotto645 = listOf(
            listOf(7, 14, 22, 25, 28, 38),  // 미당첨
        )

        val result = useCase(LottoType.L645, myLotto645.copy(numbers = lotto645)).getOrThrow()

        Assert.assertEquals(emptyList<List<Int>>(), result.winningNumbers)
    }

    @Test
    fun `미당첨된 연금복권720일 경우, 당첨 결과의 isWinner는 false이다`() = runTest {
        val firstNumber = 5
        val lotto720 = listOf(1, 2, 3, 4, 5, 6)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = firstNumber, numbers = lotto720)).getOrThrow()

        Assert.assertFalse(result.isWinner)
    }

    @Test
    fun `당첨된 연금복권720일 경우, 당첨 결과의 isWinner는 true이다`() = runTest {
        val firstNumber = 4
        val lotto720 = listOf(0, 1, 5, 1, 9, 2)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = firstNumber, numbers = lotto720)).getOrThrow()

        Assert.assertTrue(result.isWinner)
    }

    @Test
    fun `보너스 당첨된 연금복권720일 경우, 당첨 결과의 isWinner는 true이다`() = runTest {
        val lotto720 = listOf(5, 4, 9, 4, 1, 7)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = 1, numbers = lotto720)).getOrThrow()

        Assert.assertTrue(result.isWinner)
    }

    @Test
    fun `1등에 당첨된 연금복권720일 경우, winningRanks에 FIRST가 포함되어야 한다`() = runTest {
        val firstNumber = 4
        val lotto720 = listOf(0, 1, 5, 1, 9, 2)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = firstNumber, numbers = lotto720)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.L720_FIRST))
    }

    @Test
    fun `2등에 당첨된 연금복권720일 경우, winningRanks에 SECOND가 포함되어야 한다`() = runTest {
        val firstNumber = 5
        val lotto720 = listOf(0, 1, 5, 1, 9, 2)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = firstNumber, numbers = lotto720)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.L720_SECOND))
    }

    @Test
    fun `3등에 당첨된 연금복권720일 경우, winningRanks에 THIRD가 포함되어야 한다`() = runTest {
        val firstNumber = 5
        val lotto720 = listOf(3, 1, 5, 1, 9, 2)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = firstNumber, numbers = lotto720)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.L720_THIRD))
    }

    @Test
    fun `4등에 당첨된 연금복권720일 경우, winningRanks에 FOURTH가 포함되어야 한다`() = runTest {
        val firstNumber = 5
        val lotto720 = listOf(3, 4, 5, 1, 9, 2)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = firstNumber, numbers = lotto720)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.L720_FOURTH))
    }

    @Test
    fun `5등에 당첨된 연금복권720일 경우, winningRanks에 FIFTH가 포함되어야 한다`() = runTest {
        val firstNumber = 5
        val lotto720 = listOf(3, 4, 6, 1, 9, 2)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = firstNumber, numbers = lotto720)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.L720_FIFTH))
    }

    @Test
    fun `6등에 당첨된 연금복권720일 경우, winningRanks에 SIXTH가 포함되어야 한다`() = runTest {
        val firstNumber = 5
        val lotto720 = listOf(3, 4, 6, 8, 9, 2)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = firstNumber, numbers = lotto720)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.L720_SIXTH))
    }

    @Test
    fun `7등에 당첨된 연금복권720일 경우, winningRanks에 SEVENTH가 포함되어야 한다`() = runTest {
        val firstNumber = 5
        val lotto720 = listOf(3, 4, 6, 8, 7, 2)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = firstNumber, numbers = lotto720)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.L720_SEVENTH))
    }

    @Test
    fun `당첨된 연금복권720일 경우, winningNumbers에 당첨 번호가 존재한다`() = runTest {
        val firstNumber = 5
        val lotto720 = listOf(3, 4, 6, 8, 7, 2)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = firstNumber, numbers = lotto720)).getOrThrow()

        Assert.assertEquals(listOf(listOf(firstNumber) + lotto720), result.winningNumbers)
    }

    @Test
    fun `보너스에 당첨된 연금복권720일 경우, winningRanks에 BONUS가 포함되어야 한다`() = runTest {
        val firstNumber = 5
        val lotto720 = listOf(5, 4, 9, 4, 1, 7)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = firstNumber, numbers = lotto720)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.L720_BONUS))
    }

    @Test
    fun `미당첨된 연금복권720일 경우, winningRanks에 NONE가 포함되어야 한다`() = runTest {
        val firstNumber = 5
        val lotto720 = listOf(3, 4, 6, 8, 7, 8)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = firstNumber, numbers = lotto720)).getOrThrow()

        Assert.assertTrue(result.winningRanks.contains(LottoRank.NONE))
    }

    @Test
    fun `미당첨된 연금복권720일 경우, winningNumbers에 당첨 번호가 존재하지 않는다`() = runTest {
        val firstNumber = 5
        val lotto720 = listOf(3, 4, 6, 8, 7, 8)

        val result = useCase(LottoType.L720, myLotto720.copy(firstNumber = firstNumber, numbers = lotto720)).getOrThrow()

        Assert.assertEquals(listOf<List<Int>>(emptyList()), result.winningNumbers)
    }

    companion object {
        private val myLotto645 = MyLotto645Info(round = 1175, numbers = emptyList())
        private val myLotto720 = MyLotto720Info(round = 266, firstNumber = 0, numbers = emptyList())
    }
}