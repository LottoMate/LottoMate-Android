package com.lottomate.lottomate.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.error.LottoMateErrorType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel (
    private val errorHandler: LottoMateErrorHandler,
) : ViewModel() {
    private var _errorFlow = MutableSharedFlow<LottoMateErrorType>()
    val errorFlow: SharedFlow<LottoMateErrorType> get() = _errorFlow.asSharedFlow()

    protected fun handleException(throwable: Throwable) {
        val errorType = errorHandler.handleError(throwable)

        viewModelScope.launch {
            _errorFlow.emit(errorType)
        }
    }
}