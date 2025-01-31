package com.lottomate.lottomate.di

import com.lottomate.lottomate.data.local.repository.RandomLottoRepository
import com.lottomate.lottomate.data.remote.repository.InterviewRepositoryImpl
import com.lottomate.lottomate.data.remote.repository.LottoInfoRepositoryImpl
import com.lottomate.lottomate.data.remote.repository.StoreRepositoryImpl
import com.lottomate.lottomate.domain.repository.InterviewRepository
import com.lottomate.lottomate.domain.repository.LottoInfoRepository
import com.lottomate.lottomate.domain.repository.StoreRepository
import com.lottomate.lottomate.domain.repository.local.RandomLottoRepositoryImpl
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

    @Binds
    @Singleton
    abstract fun bindStoreRepository(
        storeRepositoryImpl: StoreRepositoryImpl
    ): StoreRepository

    @Binds
    @Singleton
    abstract fun bindRandomLottoRepository(
        randomLottoRepositoryImpl: RandomLottoRepositoryImpl
    ): RandomLottoRepository

    @Binds
    @Singleton
    abstract fun bindInterviewRepository(
        interviewRepositoryImpl: InterviewRepositoryImpl
    ): InterviewRepository
}