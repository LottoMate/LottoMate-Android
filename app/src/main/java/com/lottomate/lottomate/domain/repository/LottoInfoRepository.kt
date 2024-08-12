package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import kotlinx.coroutines.flow.Flow

interface LottoInfoRepository {
    fun getLatestLottoInfo(): Flow<Map<Int, LottoInfo>>
}