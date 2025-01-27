package com.lottomate.lottomate.presentation.screen.login

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.lottomate.lottomate.BuildConfig
import com.lottomate.lottomate.data.remote.api.LoginApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginApi: LoginApi,
) : ViewModel() {
    var latestLoginType = mutableStateOf(LatestLoginType.GOOGLE)
        private set

    fun loginWithGoogleClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(
                Scope("https://www.googleapis.com/auth/userinfo.email"),
                Scope("https://www.googleapis.com/auth/userinfo.profile"),
                Scope("openid")
            )
            .requestServerAuthCode(BuildConfig.GOOGLE_OAUTH_CLIENT_ID)
            .requestIdToken(BuildConfig.GOOGLE_OAUTH_CLIENT_ID)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, gso)
    }

    fun loginWithGoogle(idToken: String, accessToken: String) {
        Log.i("LoginViewModel", "loginWithGoogle: $idToken / $accessToken")

        viewModelScope.launch {
            val result = loginApi.loginWithGoogle(
                idToken = idToken,
                accessToken = accessToken
            )

            if (result.isSuccessful) {
//                Log.d("Google Login", "loginWithGoogle: ${result.body()?.string()}")

//                Log.d("Google Login", "loginWithGoogle(code): ${result.code().toString()}")
//                Log.d("Google Login", "loginWithGoogle(header): ${result.headers().toString()}")
//                Log.d("Google Login", "loginWithGoogle(body): ${result.body().toString()}")
                val testResult = loginApi.loginTest("$idToken")

                if (testResult.isSuccessful) {
                    Log.d("Google Login", "loginWithTest: ${testResult.body()?.string()}")
                }
            }
//
//            val testResult = loginApi.loginTest("Bearer $idToken")
//            Log.d("Google Login", "loginWithGoogle: ${testResult.body()?.string()}")
        }
    }
}

enum class LatestLoginType {
    KAKAO, NAVER, GOOGLE,
}