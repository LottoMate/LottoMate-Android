package com.lottomate.lottomate.presentation.screen.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.data.error.LottoMateErrorHandler
import com.lottomate.lottomate.data.remote.repository.testUserProfile
import com.lottomate.lottomate.domain.model.LoginType
import com.lottomate.lottomate.domain.repository.UserRepository
import com.lottomate.lottomate.presentation.model.LoginTypeUiModel
import com.lottomate.lottomate.presentation.screen.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    errorHandler: LottoMateErrorHandler,
    private val userRepository: UserRepository,
) : BaseViewModel(errorHandler) {
    var latestLoginType = mutableStateOf<LoginTypeUiModel?>(null)
        private set

    private val _effect = Channel<Boolean>()
    val effect = _effect.receiveAsFlow()

    init {
        checkLatestLoginInfo()
    }

    // TODO : 수정 예정
    fun loginWithEmail() {
        viewModelScope.launch {
            runCatching {
                userRepository.setLatestLoginInfo(LoginType.EMAIL)
            }
                .onSuccess {
                    userRepository.setUserProfile(testUserProfile)
                    _effect.send(true)
                }
                .onFailure { handleException(it) }
        }
    }

    private fun checkLatestLoginInfo() {
        viewModelScope.launch {
            LottoMateDataStore.getLatestLoginInfo()?.let {
                latestLoginType.value = LoginTypeUiModel.fromType(it.type)
            }
        }
    }
}