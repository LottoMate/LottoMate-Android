package com.lottomate.lottomate.data.error

/**
 * 앱에서 발생할 수 있는 오류 타입
 */
sealed interface LottoMateErrorType {
    data object Network : LottoMateErrorType
    data object LocationUpdateFailed : LottoMateErrorType

    data object Unknown : LottoMateErrorType
}