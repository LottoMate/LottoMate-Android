package com.lottomate.lottomate.presentation.screen.map.model

enum class StoreListFilter {
    DISTANCE,
    RANK;

    companion object {
        fun findFromOrdinal(ordinal: Int): StoreListFilter {
            return entries.find { it.ordinal == ordinal } ?: DISTANCE
        }
    }
}