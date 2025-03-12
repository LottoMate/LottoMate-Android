package com.lottomate.lottomate.data.model

enum class LottoType(val num: Int, val kr: String) {
    L645(
        num = 645,
        kr = "로또"
    ),
    L720(
        num = 720,
        kr =  "연금복권"
    ),
    S2000(
        num = 2_000,
        kr = "스피또"
    ),
    S1000(
        num = 1_000,
        kr = "스피또"
    ),
    S500(
        num = 500,
        kr = "스피또"
    );

    companion object {
        fun findLottoType(index: Int): LottoType = entries[index]
        fun findLottoTypeByName(name: String): LottoType {
            return when {
                name.contains(L645.num.toString()) -> L645
                name.contains(L720.num.toString()) -> L720
                name.contains(S2000.num.toString()) -> S2000
                name.contains(S1000.num.toString()) -> S1000
                name.contains(S500.num.toString()) -> S500
                else -> LottoType.L645
            }
        }
        fun getLottoNameByType(type: LottoType): String = type.kr
    }
}