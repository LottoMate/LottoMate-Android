package com.lottomate.lottomate.domain.repository.local

import com.lottomate.lottomate.data.local.api.RandomLottoDao
import com.lottomate.lottomate.data.local.entity.RandomLotto
import com.lottomate.lottomate.data.local.repository.RandomLottoRepository
import com.lottomate.lottomate.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RandomLottoRepositoryImpl @Inject constructor(
    private val randomLottoDao: RandomLottoDao,
) : RandomLottoRepository {
    override fun getAllRandomLotto(): Flow<List<RandomLotto>> = flow {
        emit(randomLottoDao.getAllRandomLotto())
    }

    override suspend fun insertRandomLotto(randomNumbers: List<Int>) {
        val randomLotto = RandomLotto(randomNumbers = randomNumbers, createAt = DateUtils.getCurrentDate())

        coroutineScope {
            withContext(Dispatchers.IO) {
                randomLottoDao.insertRandomLotto(randomLotto)
            }
        }
    }
}