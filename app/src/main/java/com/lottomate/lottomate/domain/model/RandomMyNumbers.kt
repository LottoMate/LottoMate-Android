package com.lottomate.lottomate.domain.model

data class RandomMyNumbers(
    val key: Int,
    val numbers: List<Int>,
    val date: String,
)
