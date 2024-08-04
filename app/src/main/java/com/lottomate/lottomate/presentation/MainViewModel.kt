package com.lottomate.lottomate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lottomate.lottomate.domain.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val testRepository: TestRepository,
) : ViewModel() {
    private var _testData = MutableStateFlow("")
    val testData: StateFlow<String> get() = _testData

    fun getNetworkTestData() {
        viewModelScope.launch {
            testRepository.getTest().collectLatest { result ->
                _testData.update { result }
            }
        }
    }
}