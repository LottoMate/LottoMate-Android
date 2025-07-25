package com.lottomate.lottomate.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lottomate.lottomate.data.local.api.RandomLottoDao
import com.lottomate.lottomate.data.local.api.RandomMyNumbersDao
import com.lottomate.lottomate.data.local.entity.RandomLotto
import com.lottomate.lottomate.data.local.entity.RandomMyNumbersEntity
import com.lottomate.lottomate.utils.IntListConverter

@Database(
    entities = [RandomLotto::class, RandomMyNumbersEntity::class],
    version = 2,
    exportSchema = false,
)
@TypeConverters(
    value = [IntListConverter::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun randomLottoDao(): RandomLottoDao
    abstract fun randomMyNumbersDao(): RandomMyNumbersDao
}