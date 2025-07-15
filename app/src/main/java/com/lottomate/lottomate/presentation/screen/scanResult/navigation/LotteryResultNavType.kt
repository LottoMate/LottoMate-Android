package com.lottomate.lottomate.presentation.screen.scanResult.navigation

import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.lottomate.lottomate.presentation.screen.scanResult.model.LotteryInputType
import com.lottomate.lottomate.presentation.screen.scanResult.model.LotteryResultFrom
import com.lottomate.lottomate.presentation.screen.scanResult.model.MyLottoInfo
import kotlinx.serialization.json.Json

val LotteryResultFromType = object : NavType<LotteryResultFrom>(
    isNullableAllowed = false
) {
    override fun put(
        bundle: Bundle,
        key: String,
        value: LotteryResultFrom
    ) {
        bundle.putParcelable(key, value)
    }

    override fun get(
        bundle: Bundle,
        key: String
    ): LotteryResultFrom? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, LotteryResultFrom::class.java) as LotteryResultFrom
        } else {
            bundle.getParcelable(key) as LotteryResultFrom?
        }
    }

    override fun parseValue(value: String): LotteryResultFrom {
        return Json.decodeFromString<LotteryResultFrom>(value)
    }

    override fun serializeAsValue(value: LotteryResultFrom): String {
        return Uri.encode(Json.encodeToString(value))
    }

}
val MyLottoInfoType = object : NavType<MyLottoInfo>(
    isNullableAllowed = false
) {
    override fun put(
        bundle: Bundle,
        key: String,
        value: MyLottoInfo
    ) {
        bundle.putParcelable(key, value)
    }

    override fun get(
        bundle: Bundle,
        key: String
    ): MyLottoInfo? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, MyLottoInfo::class.java) as MyLottoInfo
        } else {
            bundle.getParcelable(key) as MyLottoInfo?
        }
    }

    override fun parseValue(value: String): MyLottoInfo {
        return Json.decodeFromString<MyLottoInfo>(value)
    }

    override fun serializeAsValue(value: MyLottoInfo): String {
        return Uri.encode(Json.encodeToString(value))
    }
}

val LotteryInputTypeType = object : NavType<LotteryInputType>(
    isNullableAllowed = false
) {
    override fun put(
        bundle: Bundle,
        key: String,
        value: LotteryInputType
    ) {
        bundle.putParcelable(key, value)
    }

    override fun get(
        bundle: SavedState,
        key: String
    ): LotteryInputType? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, LotteryInputType::class.java) as LotteryInputType
        } else {
            bundle.getParcelable(key) as LotteryInputType?
        }
    }

    override fun parseValue(value: String): LotteryInputType {
        return Json.decodeFromString<LotteryInputType>(value)
    }

    override fun serializeAsValue(value: LotteryInputType): String {
        return Uri.encode(Json.encodeToString(value))
    }
}