package com.lottomate.lottomate.application

import android.app.Application
import com.lottomate.lottomate.BuildConfig
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.utils.LocationStateManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class LottoMateApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        LottoMateDataStore.init(this)
        LocationStateManager.init(this)
    }
}