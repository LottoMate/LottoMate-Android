package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.data.mapper.LottoInfoMapper
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.data.remote.api.LottoInfoApi
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LottoInfoRepositoryImpl @Inject constructor(
    private val lottoInfoApi: LottoInfoApi,
) : LottoInfoRepository {
    override fun getLatestLottoInfo(): Flow<Map<Int, LottoInfo>> = flow {
        val result = lottoInfoApi.getLatestLottoInfo()

        if (result.code == 200) {
            val lottoEntity = mutableMapOf<Int, LottoInfo>()

            result.lotto645?.let {
                lottoEntity[LottoType.L645.num] = LottoInfoMapper.toLotto645Info(it)
            }
            result.lotto720?.let {
                lottoEntity[LottoType.L720.num] = LottoInfoMapper.toLotto720Info(it)
            }

            emit(lottoEntity.toMap())
        } else {
            // TODO : 예외 처리
        }
    }
}