package com.lottomate.lottomate.data.error

import android.content.Context
import com.lottomate.lottomate.R

/**
 * 사용자에게 보여지는 Error Message를 관리하는 Provider
 */
object ErrorMessageProvider {
    fun getErrorMessage(context: Context, errorType: LottoMateErrorType): String {
        return when (errorType) {
            is LottoMateErrorType.Network -> context.getString(R.string.error_message_network)
            is LottoMateErrorType.LocationUpdateFailed -> context.getString(R.string.error_message_location_update)
            is LottoMateErrorType.Unknown -> context.getString(R.string.error_message_unknown)
        }
    }
}