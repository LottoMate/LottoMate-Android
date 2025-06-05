package com.lottomate.lottomate.data.manager.di

import com.lottomate.lottomate.data.manager.InterviewViewManager
import com.lottomate.lottomate.data.manager.InterviewViewManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {
    @Binds
    @Singleton
    abstract fun bindInterviewViewManager(
        interviewViewManagerImpl: InterviewViewManagerImpl
    ): InterviewViewManager
}