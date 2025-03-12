package com.lottomate.lottomate.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object LottoMateDataStore {
    private lateinit var context: Context
    private val Context.lottoMateDataStore by preferencesDataStore(name = "lottoMateDataStore")

    private val MAP_INIT_POPUP_KEY = booleanPreferencesKey("mapInitPopup")
    private val ONBOARDING_KEY = booleanPreferencesKey("onboarding")

    val mapInitPopupFlow: Flow<Boolean>
        get() = context.lottoMateDataStore.data.map { preference ->
            preference[MAP_INIT_POPUP_KEY] ?: false
        }

    val onBoardingFlow: Flow<Boolean>
        get() = context.lottoMateDataStore.data.map { preference ->
            preference[ONBOARDING_KEY] ?: false
        }

    /**
     * 지도 첫 진입 시 보여지는 팝업의 상태를 변경합니다.
     */
    suspend fun changeMapInitPopupState() {
        changeState(
            key = MAP_INIT_POPUP_KEY,
            state = true,
        )
    }

    /**
     * 온보딩 화면의 상태를 변경합니다.
     */
    suspend fun changeOnBoardingState() {
        changeState(
            key = ONBOARDING_KEY,
            state = true,
        )
    }

    private suspend fun changeState(
        key: Preferences.Key<Boolean>,
        state: Boolean,
    ) {
        context.lottoMateDataStore.edit { preference ->
            preference[key] = state
        }
    }

    fun init(context: Context) {
        this.context = context
    }
}