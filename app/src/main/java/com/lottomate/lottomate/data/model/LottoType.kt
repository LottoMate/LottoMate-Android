package com.lottomate.lottomate.data.model

enum class LottoType(val num: Int) {
    L645(645), L720(720), S2000(2_000), S1000(1_000), S500(500);

    companion object {
        fun findLottoType(index: Int): LottoType = entries[index]
    }
}