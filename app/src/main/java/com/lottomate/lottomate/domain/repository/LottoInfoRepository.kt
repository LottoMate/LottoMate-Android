package com.lottomate.lottomate.domain.repository

import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import kotlinx.coroutines.flow.Flow

interface LottoInfoRepository {
    val latestLottoInfo: Map<Int, LottoInfo>

    fun getLatestLottoInfo(): Flow<Map<Int, LottoInfo>>
    fun getLatestLottoInfoByLottoType(lottoType: LottoType): Flow<LottoInfo>
}