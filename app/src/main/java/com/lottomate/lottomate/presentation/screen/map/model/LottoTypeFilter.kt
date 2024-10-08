package com.lottomate.lottomate.presentation.screen.map.model

enum class LottoTypeFilter(val kr: String) {
    All("복권 전체"), Lotto645("로또"), Lotto720("연금복권"), Speetto("스피또");

    override fun toString(): String {
        return kr
    }

    companion object {
        fun find(index: Int): String {
            return entries.find { it.ordinal == index }!!.kr
        }

        fun toList(): List<String> {
            return entries.map { it.kr }.toList()
        }
    }
}