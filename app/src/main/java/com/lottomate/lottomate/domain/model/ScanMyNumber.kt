package com.lottomate.lottomate.domain.model

import com.lottomate.lottomate.data.model.LottoType

data class ScanMyNumber(
    val type: LottoType,
    val round: Int,
    val numbers: List<List<Int>>,
)
