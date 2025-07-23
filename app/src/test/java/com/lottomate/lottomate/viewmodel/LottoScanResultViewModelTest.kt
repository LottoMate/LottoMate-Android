package com.lottomate.lottomate.viewmodel

import com.lottomate.lottomate.data.error.LottoMateErrorHandlerImpl
import com.lottomate.lottomate.domain.usecase.CheckLotteryResultUseCase
import com.lottomate.lottomate.presentation.screen.result.LotteryResultUiState
import com.lottomate.lottomate.presentation.screen.result.LotteryResultViewModel
import com.lottomate.lottomate.repository.FakeInterviewRepository
import com.lottomate.lottomate.repository.FakeLottoInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LotteryResultViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var vm: LotteryResultViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        vm = LotteryResultViewModel(
            dispatcher = testDispatcher,
            errorHandler = LottoMateErrorHandlerImpl(),
            lottoInfoRepository = FakeLottoInfoRepository(),
            checkLotteryResultUseCase = CheckLotteryResultUseCase(FakeLottoInfoRepository()),
            interviewRepository = FakeInterviewRepository(),
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `미발표된 로또645일 경우, UiState는 NotYet이다`() = runTest {
        // When
        vm.getLottoResult(MOCK_LOTTO645)

        // Then
        advanceUntilIdle()
        val state = vm.state.value
        Assert.assertTrue(state is LotteryResultUiState.NotYet)
    }

    @Test
    fun `발표된 로또645일 경우, UiState는 Success이다`() = runTest {
        // When
        vm.getLottoResult(MOCK_LOTTO645_ANNOUNCEMENT)

        // Then
        advanceUntilIdle()
        val state = vm.state.value
        Assert.assertTrue(state is LotteryResultUiState.Success)
    }

    @Test
    fun `미발표된 연금복권720일 경우, UiState는 NotYet이다`() = runTest {
        // When
        vm.getLottoResult(MOCK_LOTTO720)

        // Then
        advanceUntilIdle()
        val state = vm.state.value
        Assert.assertTrue(state is LotteryResultUiState.NotYet)
    }

    @Test
    fun `발표된 연금복권720일 경우, UiState는 Success이다`() = runTest {
        // When
        vm.getLottoResult(MOCK_LOTTO720_ANNOUNCEMENT)

        // Then
        advanceUntilIdle()
        val state = vm.state.value
        Assert.assertTrue(state is LotteryResultUiState.Success)
    }

    companion object {
        private const val MOCK_LOTTO645 = "http://m.dhlottery.co.kr/?v=1178q131518222831q040710163643q012029323941m111214172128q0102151821321289734360"
        private const val MOCK_LOTTO645_ANNOUNCEMENT = "http://m.dhlottery.co.kr/?v=1075q131518222831q040710163643q012029323941m111214172128q0102151821321289734360"
        private const val MOCK_LOTTO720 = "http://qr.dhlottery.co.kr/?v=pd1202701s890966"
        private const val MOCK_LOTTO720_ANNOUNCEMENT = "http://qr.dhlottery.co.kr/?v=pd1202661s890966"
    }
}