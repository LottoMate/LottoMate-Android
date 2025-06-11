package com.lottomate.lottomate.domain.model

import com.lottomate.lottomate.data.model.LottoType

enum class LottoRank(
    val group: Group? = null,
    val count: Int,
    val bonus: Boolean = false,
) {
    L645_FIRST(Group.L645, 6),
    L645_SECOND(Group.L645, 5, true),
    L645_THIRD(Group.L645, 5),
    L645_FOURTH(Group.L645, 4),
    L645_FIFTH(Group.L645, 3),

    L720_FIRST(Group.L720, 7),
    L720_SECOND(Group.L720, 6),
    L720_THIRD(Group.L720, 5),
    L720_FOURTH(Group.L720, 4),
    L720_FIFTH(Group.L720, 3),
    L720_SIXTH(Group.L720, 2),
    L720_SEVENTH(Group.L720, 1),
    L720_BONUS(Group.L720, 6, true),

    NONE(count = 0, bonus = false);

    enum class Group(val type: LottoType) {
        L645(LottoType.L645),
        L720(LottoType.L720),
    }

    companion object {
        fun getLotto645Rank(count: Int, isbonus: Boolean): LottoRank {
            return entries
                .firstOrNull { it.group == Group.L645 && it.count == count && it.bonus == isbonus }
                ?: NONE
        }

        fun getLotto720Rank(count: Int, isbonus: Boolean): LottoRank {
            return entries
                .firstOrNull { it.group == Group.L720 && it.count == count && it.bonus == isbonus }
                ?: NONE
        }
    }
}