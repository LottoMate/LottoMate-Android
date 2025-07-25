package com.lottomate.lottomate.data.local.repository

import com.lottomate.lottomate.data.local.api.RandomLottoDao
import com.lottomate.lottomate.data.local.entity.RandomLotto
import com.lottomate.lottomate.domain.repository.local.RandomLottoRepository
import com.lottomate.lottomate.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RandomLottoRepositoryImpl @Inject constructor(
    private val randomLottoDao: RandomLottoDao,
) : RandomLottoRepository {
    private val _dataChanged = MutableSharedFlow<Unit>()
    override val dataChanged: SharedFlow<Unit> get() = _dataChanged.asSharedFlow()

    override suspend fun fetchAllRandomLotto(): List<RandomLotto> = randomLottoDao.fetchAllRandomLotto()

    override fun fetchAllRandomLottoOnlyNumbers(): Flow<List<List<Int>>> = flow {
        val result = randomLottoDao.fetchAllRandomLotto().map { randomLottoList ->
            randomLottoList.randomNumbers
        }

        emit(result)
    }

    override suspend fun insertRandomLotto(randomNumbers: List<Int>) {
        val randomLotto =
            RandomLotto(randomNumbers = randomNumbers, createAt = DateUtils.getCurrentDate())

        coroutineScope {
            withContext(Dispatchers.IO) {
                randomLottoDao.insertRandomLotto(randomLotto)
            }
        }

        _dataChanged.emit(Unit)
    }

    override suspend fun deleteAllRandomLotto() {
        randomLottoDao.deleteAllRandomLotto()

        _dataChanged.emit(Unit)
    }
    override suspend fun deleteOneOfRandomLotto(key: Int) {
        randomLottoDao.deleteOneOfRandomLotto(key)

        _dataChanged.emit(Unit)
    }
}