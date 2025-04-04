package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import kotlinx.coroutines.flow.Flow

interface LottoInfoRepository {
    val allLatestLottoRound: Map<Int, Int>
    val allLatestLottoInfo: Flow<Map<Int, LottoInfo>>

    suspend fun fetchAllLatestLottoInfo()
    fun fetchLottoInfo(lottoType: Int, lottoRndNum: Int? = null): Flow<LottoInfo>
}