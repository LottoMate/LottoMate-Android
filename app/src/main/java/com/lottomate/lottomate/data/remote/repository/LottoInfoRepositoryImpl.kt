package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.data.mapper.LottoInfoMapper
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.data.remote.api.LottoInfoApi
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LatestRoundInfo
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.SpeettoMockDatas
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class LottoInfoRepositoryImpl @Inject constructor(
    private val lottoInfoApi: LottoInfoApi,
) : LottoInfoRepository {
    private val _latestLottoRoundInfo = MutableStateFlow<Map<Int, LatestRoundInfo>>(emptyMap())
    private val _allLatestLottoInfo = MutableStateFlow<Map<Int, LottoInfo>>(emptyMap())
    override val latestLottoRoundInfo: StateFlow<Map<Int, LatestRoundInfo>>
        get() = _latestLottoRoundInfo.asStateFlow()
    override val allLatestLottoInfo: Flow<Map<Int, LottoInfo>>
        get() = _allLatestLottoInfo.asStateFlow()

    override suspend fun fetchAllLatestLottoInfo() {
        try {
            val result = lottoInfoApi.getAllLatestLottoInfo()

            if (result.code == 200) {
                val lottoRound = mutableMapOf<Int, LatestRoundInfo>()
                val lottoInfos = mutableMapOf<Int, LottoInfo>()

                result.lotto645?.let {
                    lottoInfos[LottoType.L645.num] = LottoInfoMapper.toLotto645Info(it)
                    lottoRound[LottoType.L645.num] = LatestRoundInfo(it.drwNum, it.drwDate)
                }
                result.lotto720?.let {
                    lottoInfos[LottoType.L720.num] = LottoInfoMapper.toLotto720Info(it)
                    lottoRound[LottoType.L720.num] = LatestRoundInfo(it.drwNum, it.drwDate)
                }

                _allLatestLottoInfo.update { lottoInfos.toMap() }
                _latestLottoRoundInfo.update { lottoRound }
            } else {
                // TODO : 예외 처리

                val lottoRound = mutableMapOf<Int, LatestRoundInfo>()
                lottoRound[LottoType.L720.num] = LatestRoundInfo(256, "2025-03-27")
                lottoRound[LottoType.L645.num] = LatestRoundInfo(1165, "2025-03-29")
                _latestLottoRoundInfo.update { lottoRound }
            }
        } catch (e: Exception) {
            val lottoRound = mutableMapOf<Int, LatestRoundInfo>()
            lottoRound[LottoType.L720.num] = LatestRoundInfo(256, "2025-03-27")
            lottoRound[LottoType.L645.num] = LatestRoundInfo(1165, "2025-03-29")
            _latestLottoRoundInfo.update { lottoRound }
        }
    }

    override fun fetchLottoInfo(lottoType: Int, lottoRndNum: Int?): Flow<LottoInfo> = flow {
        when (lottoType) {
            LottoType.L645.num, LottoType.L720.num -> {
                val lottoRound = lottoRndNum ?: latestLottoRoundInfo.value.getValue(lottoType).round
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