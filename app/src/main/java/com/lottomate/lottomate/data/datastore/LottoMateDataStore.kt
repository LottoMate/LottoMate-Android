package com.lottomate.lottomate.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

object LottoMateDataStore {
    private var context: Context? = null
    private val Context.lottoMateDataStore by preferencesDataStore(name = "lottoMateDataStore")

    private val MAP_INIT_POPUP_KEY = booleanPreferencesKey("mapInitPopup")

    val mapInitPopupFlow = context?.lottoMateDataStore?.data
        ?.map { preference ->
            preference[MAP_INIT_POPUP_KEY] ?: false
        }

    suspend fun changeMapInitPopupState(context: Context) {
        this.context = context
        context.lottoMateDataStore.edit { preference ->
            preference[MAP_INIT_POPUP_KEY] = true
        }
    }
}