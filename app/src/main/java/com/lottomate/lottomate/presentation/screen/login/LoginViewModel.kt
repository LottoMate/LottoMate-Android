package com.lottomate.lottomate.presentation.screen.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

) : ViewModel() {
    var latestLoginType = mutableStateOf(LatestLoginType.GOOGLE)
        private set
}

enum class LatestLoginType {
    KAKAO, NAVER, GOOGLE,
}