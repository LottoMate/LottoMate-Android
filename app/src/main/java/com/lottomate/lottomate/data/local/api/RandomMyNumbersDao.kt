package com.lottomate.lottomate.data.local.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lottomate.lottomate.data.local.entity.RandomMyNumbersEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RandomMyNumbersDao {
    @Query("SELECT * FROM RandomMyNumbers")
    fun fetchAllRandomMyNumbers(): Flow<List<RandomMyNumbersEntity>>

    @Insert
    suspend fun insertRandomMyNumbers(randomMyNumbersEntity: RandomMyNumbersEntity)

    @Query("DELETE FROM RandomMyNumbers WHERE `key` = :key")
    suspend fun deleteRandomMyNumbers(key: Int)
}