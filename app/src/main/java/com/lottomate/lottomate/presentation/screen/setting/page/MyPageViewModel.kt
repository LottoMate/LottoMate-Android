package com.lottomate.lottomate.presentation.screen.setting.page

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    fun logOut() {
        viewModelScope.launch {
            userRepository.setUserProfile(null)
                .onFailure {
                    Log.d("MyPageVM", "logOut: ${it.stackTraceToString()}")
                }
        }
    }
}