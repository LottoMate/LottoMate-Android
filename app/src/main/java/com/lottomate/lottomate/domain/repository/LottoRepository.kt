package com.lottomate.lottomate.domain.repository

interface LottoRepository {
    suspend fun fetchLottoWinResultByRound(round: String): List<Int>
}