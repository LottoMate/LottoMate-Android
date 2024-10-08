package com.lottomate.lottomate.di

import com.lottomate.lottomate.data.remote.repository.LottoInfoRepositoryImpl
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindLottoInfoRepository(
        lottoInfoRepositoryImpl: LottoInfoRepositoryImpl
    ): LottoInfoRepository
}