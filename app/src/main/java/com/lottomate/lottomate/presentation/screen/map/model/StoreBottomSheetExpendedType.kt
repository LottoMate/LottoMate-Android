package com.lottomate.lottomate.presentation.screen.map.model

/**
 * 복권 판매점 BottomSheet 의 확장/축소 타입
 */
enum class StoreBottomSheetExpendedType(val ratio: Float) {
    COLLAPSED(0f),
    HALF(0.6f),
    FULL(0.9f),
}