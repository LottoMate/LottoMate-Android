package com.lottomate.lottomate.domain.model

import com.lottomate.lottomate.BuildConfig

data class AppVersionInfo(
    val currentVersion: String = BuildConfig.VERSION_NAME,
    val latestVersion: String = BuildConfig.VERSION_NAME,
    val isForceUpdate: Boolean = false,
    val isNeedUpdate: Boolean = false,
)