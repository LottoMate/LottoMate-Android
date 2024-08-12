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
    private val _latestLottoInfo = mutableMapOf<Int, LottoInfo>()
    override val latestLottoInfo: Map<Int, LottoInfo>
        get() = _latestLottoInfo.toMap()

    override fun fetchLatestLottoInfo(): Flow<Map<Int, LottoInfo>> = flow {
        val result = lottoInfoApi.getLatestLottoInfo()

        if (result.code == 200) {
            val lottoEntity = mutableMapOf<Int, LottoInfo>()

            result.lotto645?.let {
                lottoEntity[LottoType.L645.num] = LottoInfoMapper.toLotto645Info(it)
            }
            result.lotto720?.let {
                lottoEntity[LottoType.L720.num] = LottoInfoMapper.toLotto720Info(it)
            }

            _latestLottoInfo.putAll(lottoEntity)
            emit(lottoEntity.toMap())
        } else {
            // TODO : 예외 처리
        }
    }

    override fun getLatestLottoInfoByLottoType(lottoType: LottoType): Flow<LottoInfo> = flow {
        if (latestLottoInfo.isEmpty()) fetchLatestLottoInfo()

        val latestLottoInfo = when (lottoType) {
            LottoType.L645 -> latestLottoInfo.getValue(LottoType.L645.num)
            LottoType.L720 -> latestLottoInfo.getValue(LottoType.L720.num)
            else -> {
                // TODO : 스피또 (수정예정)
                latestLottoInfo.getValue(LottoType.L645.num)
            }
        }

        emit(latestLottoInfo)
    }
}