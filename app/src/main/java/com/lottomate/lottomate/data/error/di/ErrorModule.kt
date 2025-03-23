package com.lottomate.lottomate.data.error.di

import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.error.LottoMateErrorHandlerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorModule {
    @Binds
    @Singleton
    abstract fun bindErrorHandler(
        lottoMateErrorHandlerImpl: LottoMateErrorHandlerImpl
    ): LottoMateErrorHandler
}