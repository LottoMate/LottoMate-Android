package com.lottomate.lottomate.data.remote.repository

import com.lottomate.lottomate.domain.repository.LottoNumberRepository
import com.lottomate.lottomate.presentation.screen.pocket.random.model.RandomLottoNumber
import com.lottomate.lottomate.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class RemoteLottoNumberRepository @Inject constructor(

) : LottoNumberRepository {
    private val _lottoNumbers = MutableStateFlow<List<RandomLottoNumber>>(emptyList())
    override val lottoNumbers: StateFlow<List<RandomLottoNumber>>
        get() = _lottoNumbers.asStateFlow()

    override suspend fun getLottoNumbers(): Flow<List<RandomLottoNumber>> {
        return lottoNumbers
    }

    override suspend fun saveLottoNumber(lottoNumbers: List<Int>) {
        val lastId = _lottoNumbers.value.lastOrNull()?.id ?: 0

        val randomLottoNumber = RandomLottoNumber(
            id = lastId.plus(1),
            numbers = lottoNumbers,
            date = DateUtils.getCurrentDate()
        )

        _lottoNumbers.update {
            it + randomLottoNumber
        }
    }

    override suspend fun removeLottoNumber(id: Int) {
        _lottoNumbers.update {
            it.filter { randomNumber -> randomNumber.id != id }
        }
    }
}