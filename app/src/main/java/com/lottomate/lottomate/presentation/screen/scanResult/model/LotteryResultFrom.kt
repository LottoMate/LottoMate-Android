package com.lottomate.lottomate.presentation.screen.scanResult.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
enum class LotteryResultFrom : Parcelable {
    SCAN,
    REGISTER,
    MY_NUMBER,
}

@Serializable
@Parcelize
enum class LotteryInputType : Parcelable {
    ONLY645,
    ONLY720,
    BOTH;

    companion object {
        fun get(has645: Boolean, has720: Boolean): LotteryInputType? {
            return when {
                has645 && has720 -> BOTH
                has645 -> ONLY645
                has720 -> ONLY720
                else -> null
            }
        }
    }
}