package com.lottomate.lottomate.presentation.screen.map

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.domain.repository.UserRepository
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import com.lottomate.lottomate.presentation.screen.map.model.StoreInfo
import com.lottomate.lottomate.presentation.screen.map.model.StoreListFilter
import com.lottomate.lottomate.utils.ClipboardUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreBottomSheetViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val storeRepository: StoreRepository,
    private val userRepository: UserRepository,
) : BaseViewModel(errorHandler) {
    val userProfile = userRepository.userProfile
    private val _state = MutableStateFlow(StoreBottomSheetUiState.State())
    val state get() = _state.asStateFlow()

    init {
        initState()
    }

    private fun initState() {
        viewModelScope.launch {
            runCatching {
                storeRepository.stores
                    .catch { handleException(it) }
                    .distinctUntilChanged()
                    .collectLatest { collectData ->
                        _state.update { it.copy(stores = collectData) }
                    }
            }.onFailure { handleException(it) }
        }
    }

    fun setFavoriteStore(key: Int) = storeRepository.setFavoriteStore(key)

    fun changeStoreListFilter(newStoreListFilter: StoreListFilter) {
        _state.update {
            it.copy(selectedFilter = newStoreListFilter)
        }
    }

    fun copyStoreInfo(context: Context, store: StoreInfo, onSuccess: (String) -> Unit) {
        val copyText = "지점명: ${store.storeName}\n주소: ${store.address}"

        ClipboardUtils.copyToClipboard(
            context = context,
            copyText = copyText,
            onSuccess = { onSuccess("로또 판매점 주소를 복사했어요") }
        )
    }
}

sealed interface StoreBottomSheetUiState {
    data class State(
        val stores: List<StoreInfo> = emptyList(),
        val selectedFilter: StoreListFilter = StoreListFilter.DISTANCE,
        val showWinningHistory: Boolean = false,
    ) : StoreBottomSheetUiState
}