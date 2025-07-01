package com.lottomate.lottomate.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class LoginType {
    EMAIL,
    KAKAO,
    NAVER,
    GOOGLE,
}