package com.lottomate.lottomate.presentation.screen.setting

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.data.remote.repository.testUserProfile
import com.lottomate.lottomate.domain.repository.UserRepository
import com.lottomate.lottomate.presentation.model.LoginTypeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    val userProfile = userRepository.userProfile
    var latestLoginType = mutableStateOf<LoginTypeUiModel?>(null)
        private set

    init {
        viewModelScope.launch {
            LottoMateDataStore.getLatestLoginInfo()?.let {
                latestLoginType.value = LoginTypeUiModel.fromType(it.type)
            }
        }
    }

    fun loginWithEmail() {
        viewModelScope.launch {
            runCatching {
                userRepository.setUserProfile(testUserProfile)
            }
                .onFailure {
                    Log.d("SettingVM", "loginWithEmail: ${it.stackTraceToString()}")
                }

        }
    }
}