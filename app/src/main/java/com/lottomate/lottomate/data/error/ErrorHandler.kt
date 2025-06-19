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
            is InvalidLottoQRFormatException -> {
                Log.d("🔴 Error ", throwable.message ?: throwable.stackTraceToString())
                LottoMateErrorType.QRParsingException
            }
            else -> {
                Log.d("🔴 Error ", throwable.stackTraceToString())

                LottoMateErrorType.Unknown
            }
        }
    }
}