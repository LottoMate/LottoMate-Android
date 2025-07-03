package com.lottomate.lottomate.data.model

enum class LottoType(
    val code: String,
    val num: Int,
    val kr: String,
    val displayName: String,
    val group: Group,
) {
    L645(
        code = "L645",
        num = 645,
        kr = "로또",
        displayName = "로또",
        group = Group.LOTTO645,
    ),
    L720(
        code = "L720",
        num = 720,
        kr =  "연금복권",
        displayName = "연금복권",
        group = Group.LOTTO720,
    ),
    S2000(
        code = "S2000",
        num = 2_000,
        kr = "스피또",
        displayName = "스피또 2000",
        group = Group.SPEETTO,
    ),
    S1000(
        code = "S1000",
        num = 1_000,
        kr = "스피또",
        displayName = "스피또 1000",
        group = Group.SPEETTO,
    ),
    S500(
        code = "S500",
        num = 500,
        kr = "스피또",
        displayName = "스피또 500",
        group = Group.SPEETTO,
    );

    enum class Group(val displayKr: String) {
        ALL(
            displayKr = "복권 전체"
        ),
        LOTTO645(
            displayKr = "로또",
        ),
        LOTTO720(
            displayKr = "연금복권",
        ),
        SPEETTO(
            displayKr = "스피또",
        );

        companion object {
            fun getGroupsWithoutAll(): List<Group> {
                return entries.subList(ALL.ordinal + 1, entries.size)
            }

            fun getDisplayNames(groups: List<Group>): String {
                return groups.joinToString(", ") { it.displayKr }
            }

            fun selectableValues(): List<Group> {
                return entries.filter { it != LottoType.Group.ALL }
            }

            /**
             * 서버에 전달할 code (0 ~ 6) 계산
             */
            fun toServerCode(selected: List<Group>): Int {
                return when {
                    selected.containsAll(listOf(LOTTO720, SPEETTO)) -> 6
                    selected.containsAll(listOf(LOTTO645, SPEETTO)) -> 5
                    selected.containsAll(listOf(LOTTO645, LOTTO720)) -> 4
                    selected.contains(SPEETTO) -> 3
                    selected.contains(LOTTO720) -> 2
                    selected.contains(LOTTO645) -> 1
                    selected.contains(ALL) -> 0
                    else -> 0
                }
            }
        }
    }

    companion object {
        fun fromCode(code: String): LottoType = entries.firstOrNull { it.code == code } ?: L645
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
        fun findLottoTypeByCode(code: String): LottoType {
            return when {
                code.contains(L645.code) -> L645
                code.contains(L720.code) -> L720
                else -> L645
            }
        }
        fun getLottoNameByType(type: LottoType): String = type.kr
    }
}