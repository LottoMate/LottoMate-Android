package com.lottomate.lottomate.presentation.screen.setting

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.domain.model.LoginType
import com.lottomate.lottomate.presentation.model.LoginTypeUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(

) : ViewModel() {
    var latestLoginType = mutableStateOf<LoginTypeUiModel?>(null)
        private set

    init {
        viewModelScope.launch {
            LottoMateDataStore.getLatestLoginInfo()?.let {
                latestLoginType.value = LoginTypeUiModel.fromType(it.type)
            }
        }
    }
}