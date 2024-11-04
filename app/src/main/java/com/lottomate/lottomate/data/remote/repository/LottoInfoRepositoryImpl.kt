package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.data.mapper.LottoInfoMapper
import com.lottomate.lottomate.data.model.LottoType
import com.lottomate.lottomate.data.remote.api.LottoInfoApi
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.LottoInfo
import com.lottomate.lottomate.presentation.screen.lottoinfo.model.SpeettoMockDatas
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

class LottoInfoRepositoryImpl @Inject constructor(
    private val lottoInfoApi: LottoInfoApi,
) : LottoInfoRepository {
    private val _allLatestLottoRound = mutableMapOf<Int, Int>()
    override val allLatestLottoRound: Map<Int, Int>
        get() = _allLatestLottoRound.toMap()

    override suspend fun fetchAllLatestLottoInfo() {
        val result = lottoInfoApi.getAllLatestLottoInfo()

        if (result.code == 200) {
            val lottoRound = mutableMapOf<Int, Int>()

            result.lotto645?.let { lottoRound[LottoType.L645.num] = it.drwNum }
            result.lotto720?.let { lottoRound[LottoType.L720.num] = it.drwNum }

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

    override fun googleLogin(): Flow<Response<ResponseBody>> = flow {
        emit(lottoInfoApi.googleLogin())
    }
}