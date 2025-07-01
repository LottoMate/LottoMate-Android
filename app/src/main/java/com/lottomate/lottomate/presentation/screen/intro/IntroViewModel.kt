package com.lottomate.lottomate.presentation.screen.intro

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(

) : ViewModel() {
    var isLogin = mutableStateOf(false)
        private set

    init {
        checkLatestLoginInfo()
    }

    private fun checkLatestLoginInfo() {
        viewModelScope.launch {
            try {
                val latestLoginInfo = LottoMateDataStore.getLatestLoginInfo()

                isLogin.value = latestLoginInfo?.let { true } ?: false
            } catch (e: Exception) {
                isLogin.value = false
            }
        }
    }
}