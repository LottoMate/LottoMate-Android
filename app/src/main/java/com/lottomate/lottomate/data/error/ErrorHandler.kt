package com.lottomate.lottomate.data.error

import android.util.Log
import java.net.UnknownHostException
import javax.inject.Inject

interface LottoMateErrorHandler {
    fun handleError(throwable: Throwable): LottoMateErrorType
}

class LottoMateErrorHandlerImpl @Inject constructor() : LottoMateErrorHandler {
    override fun handleError(throwable: Throwable): LottoMateErrorType {
        return when (throwable) {
            is UnknownHostException -> LottoMateErrorType.Network
            else -> {
                Log.d("예상하지 못한 오류 발생", throwable.stackTraceToString())

                LottoMateErrorType.Unknown
            }
        }
    }
}