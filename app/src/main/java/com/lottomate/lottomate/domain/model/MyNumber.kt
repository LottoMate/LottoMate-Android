package com.lottomate.lottomate.domain.model

import com.lottomate.lottomate.data.model.LottoType

data class MyNumber(
    val no: Int,
    val type: LottoType,
    val drawNo: Int,
    val numbers: List<Int>,
    val isWinner: Boolean,
)