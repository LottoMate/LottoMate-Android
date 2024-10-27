package com.lottomate.lottomate.presentation.screen.login

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.lottomate.lottomate.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {
    var latestLoginType = mutableStateOf(LatestLoginType.GOOGLE)
        private set

    fun loginWithGoogleClient(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(BuildConfig.GOOGLE_OAUTH_CLIENT_ID)
            .requestIdToken(BuildConfig.GOOGLE_OAUTH_CLIENT_ID)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso)
    }
}

enum class LatestLoginType {
    KAKAO, NAVER, GOOGLE,
}