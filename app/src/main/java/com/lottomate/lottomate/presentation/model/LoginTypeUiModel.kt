package com.lottomate.lottomate.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.lottomate.lottomate.R
import com.lottomate.lottomate.domain.model.LoginType

enum class LoginTypeUiModel(
    val type: LoginType,
    @DrawableRes val iconRes: Int,
    @StringRes val descRes: Int,
) {
    EMAIL(
        type = LoginType.EMAIL,
        iconRes = R.drawable.img_kakao,
        descRes = R.string.login_desc_email,
    ),
    KAKAO(
        type = LoginType.KAKAO,
        iconRes = R.drawable.img_kakao,
        descRes = R.string.login_desc_kakao,
    ),
    NAVER(
        type = LoginType.NAVER,
        iconRes = R.drawable.img_naver,
        descRes = R.string.login_desc_naver,
    ),
    GOOGLE(
        type = LoginType.GOOGLE,
        iconRes = R.drawable.img_google,
        descRes = R.string.login_desc_google,
    );

    companion object {
        fun fromType(type: LoginType): LoginTypeUiModel? {
            return entries.find { it.type == type }
        }
    }
}