package com.lottomate.lottomate

import com.lottomate.lottomate.data.error.LottoMateErrorHandlerImpl
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.screen.map.MapViewModel
import com.lottomate.lottomate.presentation.screen.map.model.MapContract
import com.lottomate.lottomate.repository.FakeStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MapViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var vm: MapViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        vm = MapViewModel(
            errorHandler = LottoMateErrorHandlerImpl(),
            storeRepository = FakeStoreRepository()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `복권을 선택하면 선택된 복권 종류가 상태에 반영된다`() = runTest {
        // Given : 초기 상태 (모든 복권 선택)
        Assert.assertEquals(listOf(LottoType.Group.ALL), vm.state.value.selectedLotteryType)

        // When : 복권을 선택하면
        val selected = listOf(LottoType.Group.LOTTO645, LottoType.Group.SPEETTO)
        vm.handleEvent(MapContract.Event.SelectLotteryType(selected))

        // Then : 선택된 복권 종류가 반영된다
        Assert.assertEquals("로또, 스피또", LottoType.Group.getDisplayNames(vm.state.value.selectedLotteryType))
    }

    @Test
    fun `복권을 전체 선택하면 복권 전체로 표시된다`() = runTest {
        // Given
        val selected = listOf(LottoType.Group.ALL)

        // When : 복권을 전체 선택하면
        vm.handleEvent(MapContract.Event.SelectLotteryType(selected))

        // Then : 복권 전체로 표시된다
        Assert.assertEquals("복권 전체", LottoType.Group.getDisplayNames(vm.state.value.selectedLotteryType))
    }
}