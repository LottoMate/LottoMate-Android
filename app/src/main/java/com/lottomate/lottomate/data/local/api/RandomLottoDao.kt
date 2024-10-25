package com.lottomate.lottomate.data.local.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lottomate.lottomate.data.local.entity.RandomLotto
import kotlinx.coroutines.flow.Flow

@Dao
interface RandomLottoDao {
    @Query("SELECT * FROM randomlotto")
    suspend fun fetchAllRandomLotto(): List<RandomLotto>

    @Insert
    suspend fun insertRandomLotto(lotto: RandomLotto)

    @Query("DELETE FROM randomlotto")
    suspend fun deleteAllRandomLotto()

    @Query("DELETE FROM randomlotto WHERE `key` = :key")
    suspend fun deleteOneOfRandomLotto(key: Int)
}