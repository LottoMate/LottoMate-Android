package com.lottomate.lottomate.data.manager

import com.lottomate.lottomate.domain.model.AppVersionInfo
import kotlinx.coroutines.flow.StateFlow

interface AppVersionInfoManager {
    val appVersionInfo: StateFlow<AppVersionInfo>

    fun checkAppUpdate()
}