package com.lottomate.lottomate.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.lottomate.lottomate.data.model.InterviewViewEntity
import com.lottomate.lottomate.domain.model.LatestLoginInfo
import com.lottomate.lottomate.utils.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object LottoMateDataStore {
    private lateinit var context: Context
    private val Context.lottoMateDataStore by preferencesDataStore(name = "lottoMateDataStore")

    private val MAP_INIT_POPUP_KEY = booleanPreferencesKey("mapInitPopup")
    private val ONBOARDING_KEY = booleanPreferencesKey("onboarding")
    private val INTERVIEW_VIEWED_KEY = stringPreferencesKey("interviewViewed")
    private val LATEST_LOGIN_INFO_KEY = stringPreferencesKey("latestLoginInfo")

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

    suspend fun getInterviewViewed(): InterviewViewEntity {
        val raw = context.lottoMateDataStore.data.first()[INTERVIEW_VIEWED_KEY]

        val parsed = raw?.let {
            runCatching { Json.decodeFromString<InterviewViewEntity>(it) }.getOrNull()
        }

        return parsed?.let {
            if (it.date == DateUtils.getCurrentDate()) it
            else InterviewViewEntity(DateUtils.getCurrentDate(), emptySet())
        } ?: InterviewViewEntity(DateUtils.getCurrentDate(), emptySet())
    }

    suspend fun saveInterviewViewed(interviewViewEntity: InterviewViewEntity) {
        changeState(INTERVIEW_VIEWED_KEY, Json.encodeToString<InterviewViewEntity>(interviewViewEntity))
    }

    suspend fun getLatestLoginInfo(): LatestLoginInfo? {
        val raw = context.lottoMateDataStore.data.first()[LATEST_LOGIN_INFO_KEY]

        val parsed = raw?.let {
            runCatching { Json.decodeFromString<LatestLoginInfo>(it) }.getOrNull()
        }

        return parsed
    }

    suspend fun saveLatestLoginInfo(latestLoginInfo: LatestLoginInfo) {
        changeState(LATEST_LOGIN_INFO_KEY, Json.encodeToString<LatestLoginInfo>(latestLoginInfo))
    }

    private suspend fun <T> changeState(
        key: Preferences.Key<T>,
        state: T,
    ) {
        context.lottoMateDataStore.edit { preference ->
            preference[key] = state
        }
    }

    fun init(context: Context) {
        this.context = context
    }
}