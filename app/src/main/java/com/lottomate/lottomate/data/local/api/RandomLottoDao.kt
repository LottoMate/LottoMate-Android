package com.lottomate.lottomate.data.local.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lottomate.lottomate.data.local.entity.RandomLotto
import kotlinx.coroutines.flow.Flow

@Dao
interface RandomLottoDao {
    @Query("SELECT * FROM randomlotto")
    fun getAllRandomLotto(): Flow<List<RandomLotto>>

    @Insert
    fun insertRandomLotto(lotto: RandomLotto)
}