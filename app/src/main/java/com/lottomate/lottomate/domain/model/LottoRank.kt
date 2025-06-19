package com.lottomate.lottomate.domain.model

import com.lottomate.lottomate.data.model.LottoType

enum class LottoRank(
    val rank: Int,
    val group: Group? = null,
    val count: Int,
    val bonus: Boolean = false,
) {
    L645_FIRST(1, Group.L645, 6),
    L645_SECOND(2, Group.L645, 5, true),
    L645_THIRD(3, Group.L645, 5),
    L645_FOURTH(4, Group.L645, 4),
    L645_FIFTH(5, Group.L645, 3),

    L720_FIRST(1, Group.L720, 7),
    L720_SECOND(2, Group.L720, 6),
    L720_THIRD(3, Group.L720, 5),
    L720_FOURTH(4, Group.L720, 4),
    L720_FIFTH(5, Group.L720, 3),
    L720_SIXTH(6, Group.L720, 2),
    L720_SEVENTH(7, Group.L720, 1),
    L720_BONUS(8, Group.L720, 6, true),

    NONE(0, count = 0, bonus = false);

    enum class Group(val type: LottoType) {
        L645(LottoType.L645),
        L720(LottoType.L720),
    }

    companion object {
        fun getLotto645Rank(count: Int, isBonus: Boolean): LottoRank {
            return entries
                .firstOrNull { it.group == Group.L645 && it.count == count && it.bonus == isBonus }
                ?: NONE
        }

        fun getLotto720Rank(count: Int, isbonus: Boolean): LottoRank {
            return entries
                .firstOrNull { it.group == Group.L720 && it.count == count && it.bonus == isbonus }
                ?: NONE
        }
    }
}