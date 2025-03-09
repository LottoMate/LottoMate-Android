package com.lottomate.lottomate.application

import android.app.Application
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LottoMateApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        LottoMateDataStore.init(this)
    }
}