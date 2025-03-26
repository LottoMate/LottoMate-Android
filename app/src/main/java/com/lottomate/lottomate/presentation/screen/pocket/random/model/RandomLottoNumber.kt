package com.lottomate.lottomate.presentation.screen.pocket.random.model

/**
 * 랜덤 뽑기로 뽑은 로또 번호
 */
data class RandomLottoNumber(
    val id: Int = 0,
    val numbers: List<Int>,
    val date: String, // "2025-03-25" 같이 저장된다면 date가 더 어울림
)