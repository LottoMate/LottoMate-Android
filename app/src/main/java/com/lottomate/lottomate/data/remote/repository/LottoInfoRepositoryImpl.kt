package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.data.mapper.LottoInfoMapper
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.data.remote.api.LottoInfoApi
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.SpeettoMockDatas
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class LottoInfoRepositoryImpl @Inject constructor(
    private val lottoInfoApi: LottoInfoApi,
) : LottoInfoRepository {
    private val _allLatestLottoRound = mutableMapOf<Int, Int>()
    override val allLatestLottoRound: Map<Int, Int>
        get() = _allLatestLottoRound.toMap()
    private val _allLatestLottoInfo = MutableStateFlow<Map<Int, LottoInfo>>(emptyMap())
    override val allLatestLottoInfo: Flow<Map<Int, LottoInfo>>
        get() = _allLatestLottoInfo.asStateFlow()

    override suspend fun fetchAllLatestLottoInfo() {
        val result = lottoInfoApi.getAllLatestLottoInfo()

        if (result.code == 200) {
            val lottoRound = mutableMapOf<Int, Int>()
            val lottoInfos = mutableMapOf<Int, LottoInfo>()

            result.lotto645?.let {
                lottoInfos[LottoType.L645.num] = LottoInfoMapper.toLotto645Info(it)
                lottoRound[LottoType.L645.num] = it.drwNum
            }
            result.lotto720?.let {
                lottoInfos[LottoType.L720.num] = LottoInfoMapper.toLotto720Info(it)
                lottoRound[LottoType.L720.num] = it.drwNum
            }

            _allLatestLottoInfo.update {
                lottoInfos.toMap()
            }
            _allLatestLottoRound.putAll(lottoRound)
        } else {
            // TODO : 예외 처리
        }
    }

    override fun fetchLottoInfo(lottoType: Int, lottoRndNum: Int?): Flow<LottoInfo> = flow {
        when (lottoType) {
            LottoType.L645.num, LottoType.L720.num -> {
                val lottoRound = lottoRndNum ?: allLatestLottoRound.getValue(lottoType)
                val result = lottoInfoApi.fetchLottoInfo(lottoType, lottoRound)

                if (result.code == 200) {
                    result.lotto645?.let {
                        emit(LottoInfoMapper.toLotto645Info(it))
                    }

                    result.lotto720?.let {
                        emit(LottoInfoMapper.toLotto720Info(it))
                    }
                }
            }
            else -> {
                // TODO : Mock Data 사용 (추후 변경 예정)
                emit(SpeettoMockDatas)
            }
        }
    }
}