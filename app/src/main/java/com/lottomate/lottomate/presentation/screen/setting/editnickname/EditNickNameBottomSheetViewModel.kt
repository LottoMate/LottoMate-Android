package com.lottomate.lottomate.presentation.screen.setting.editnickname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditNickNameBottomSheetViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _effect = Channel<Boolean>()
    val effect = _effect.receiveAsFlow()

    private val _state = MutableStateFlow(EditNickNameBottomSheetUiState())
    val state get() = _state.asStateFlow()

    fun init(nickname: String) {
        _state.update { it.copy(nickName = nickname, errorType = null, isComplete = false) }
    }

    fun editNickName(nickName: String) {
        _state.update {
            it.copy(nickName = nickName)
        }
    }

    fun changeNickName() {
        if (state.value.nickName.length < NICKNAME_MIN_LENGTH) {
            _state.update { it.copy(errorType = EditNickNameBottomSheetUiState.ErrorType.MIN_LENGTH) }
            return
        }

        viewModelScope.launch {
            userRepository.updateUserNickName(state.value.nickName)
                .onSuccess {
                    _state.update { it.copy(isComplete = true, errorType = null) }
                    _effect.send(true)
                }
                .onFailure {
                    // TODO : 이미 존재하는 닉네임일 경우
                    _state.update {
                        it.copy(
                            errorType = EditNickNameBottomSheetUiState.ErrorType.DUPLICATED_NICKNAME,
                            isComplete = false
                        )
                    }
                }
        }
    }

    companion object {
        private const val NICKNAME_MIN_LENGTH = 2
    }
}

data class EditNickNameBottomSheetUiState(
    val nickName: String = "",
    val errorType: ErrorType? = null,
    val isComplete: Boolean = false,
    val showSnackBar: Boolean = false,
) {
    enum class ErrorType {
        MIN_LENGTH,
        DUPLICATED_NICKNAME,
    }
}