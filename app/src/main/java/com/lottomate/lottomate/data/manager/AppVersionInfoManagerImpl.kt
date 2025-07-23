package com.lottomate.lottomate.data.manager

import android.content.Context
import android.os.Build
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.lottomate.lottomate.BuildConfig
import com.lottomate.lottomate.domain.model.AppVersionInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

class AppVersionInfoManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteConfig: FirebaseRemoteConfig,
) : AppVersionInfoManager {
    private val _appVersionInfo = MutableStateFlow(AppVersionInfo())
    override val appVersionInfo: StateFlow<AppVersionInfo>
        get() = _appVersionInfo.asStateFlow()

    init {
        initConfig()
        loadLatestAppVersionInfo()
    }

    override fun checkAppUpdate() {
        try {
            remoteConfig.fetchAndActivate()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val remoteVersion = remoteConfig.getString(KEY_LATEST_VERSION)
                        val currentVersion = getPackageVersionInfo()?.first

                        currentVersion?.let { packageVersion ->
                            val (remoteMajor, remoteMinor, _) = remoteVersion.split(".")
                            val (packageMajor, packageMinor, _) = packageVersion.split(".")

                            val isSameMajor = remoteMajor == packageMajor
                            val isSameMinor = remoteMinor == packageMinor

                            _appVersionInfo.update {
                                it.copy(
                                    latestVersion = remoteVersion,
                                    currentVersion = packageVersion,
                                    isForceUpdate = !isSameMajor,
                                    isNeedUpdate = !isSameMinor,
                                )
                            }
                        }
                    } else {
                        _appVersionInfo.update { it.copy(isNeedUpdate = false, isForceUpdate = false) }
                    }
                }
        } catch (e: Exception) {
            Timber.e(e)

            _appVersionInfo.update { it.copy(isNeedUpdate = false, isForceUpdate = false) }
        }
    }

    private fun initConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) MINIMUM_FETCH_DEBUG else MINIMUM_FETCH_RELEASE
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(mutableMapOf<String, String>().apply {
            put("latestAppVersion", getPackageVersionInfo()?.first ?: BuildConfig.VERSION_NAME)
        } as Map<String, Any>).addOnCompleteListener {
            if (it.isSuccessful) {
                Timber.d("기본 값 설정 완료")
            }
        }
    }

    private fun loadLatestAppVersionInfo() {
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Timber.d("최신 버전 업데이트 완료")

                if (configUpdate.updatedKeys.contains(KEY_LATEST_VERSION)) {
                    remoteConfig.activate().addOnCompleteListener {
                        checkAppUpdate()
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Timber.e(error.stackTraceToString())
            }
        })
    }

    private fun getPackageVersionInfo(): Pair<String, Long>? {
        return try {
            val packageInfo = context.applicationContext
                .packageManager
                .getPackageInfo(PACKAGE_NAME, 0)

            val version = packageInfo.versionName

            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else packageInfo.versionCode.toLong()

            version?.let { Pair(it, versionCode) }
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    companion object {
        private const val PACKAGE_NAME = BuildConfig.APPLICATION_ID
        private const val KEY_LATEST_VERSION = "latestAppVersion"
        private const val MINIMUM_FETCH_DEBUG: Long = 1
        private const val MINIMUM_FETCH_RELEASE: Long = 3_600
    }
}