package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LatestRoundInfo
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface LottoInfoRepository {
    val latestLottoRoundInfo: StateFlow<Map<Int, LatestRoundInfo>>
    val allLatestLottoInfo: Flow<Map<Int, LottoInfo>>

    suspend fun fetchAllLatestLottoInfo()
    fun fetchLottoInfo(lottoType: Int, lottoRndNum: Int? = null): Flow<LottoInfo>
}