package com.lottomate.lottomate.di

import com.lottomate.lottomate.BuildConfig
import com.lottomate.lottomate.data.remote.api.InterviewApi
import com.lottomate.lottomate.data.remote.api.LoginApi
import com.lottomate.lottomate.data.remote.api.LottoApi
import com.lottomate.lottomate.data.remote.api.LottoInfoApi
import com.lottomate.lottomate.data.remote.api.StoreApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL: String = BuildConfig.DATABASE_BASE_URL
    private const val DHLOTTERY_BASE_URL = "https://www.dhlottery.co.kr/"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DHLottery



    private val jsonOptions = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Provides
    @Singleton
    @DHLottery
    fun provideRetrofitForDHLottery(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DHLOTTERY_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonOptions.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(jsonOptions.asConverterFactory("application/json; charset=UTF8".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(
        logger: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    fun provideLoggerInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @Singleton
    fun provideLottoInfoApi(retrofit: Retrofit): LottoInfoApi = retrofit.create(LottoInfoApi::class.java)

    @Provides
    @Singleton
    fun provideStoreApi(retrofit: Retrofit): StoreApi = retrofit.create(StoreApi::class.java)

    @Provides
    @Singleton
    fun provideLoginApi(retrofit: Retrofit): LoginApi = retrofit.create(LoginApi::class.java)

    @Provides
    @Singleton
    fun provideInterviewApi(retrofit: Retrofit): InterviewApi = retrofit.create(InterviewApi::class.java)

    @Provides
    @Singleton
    @DHLottery
    fun provideLottoApi(@DHLottery retrofit: Retrofit): LottoApi = retrofit.create(LottoApi::class.java)
}