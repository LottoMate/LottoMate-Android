package com.lottomate.lottomate.di

import android.content.Context
import androidx.room.Room
import com.lottomate.lottomate.data.local.AppDatabase
import com.lottomate.lottomate.data.local.api.RandomLottoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val LOCAL_DATABASE_NAME = "LottoMate.db"

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room
        .databaseBuilder(context, AppDatabase::class.java, LOCAL_DATABASE_NAME)
        .build()

    @Singleton
    @Provides
    fun provideRandomLottoDao(
        appDatabase: AppDatabase
    ): RandomLottoDao = appDatabase.randomLottoDao()
}