package com.lottomate.lottomate.presentation.screen.pocket.register.model

/**
 * 보관소 - 내 번호 등록
 * 번호 등록 후, 이동에 대한 Type
 *
 * None : 초기값 (이동 X)
 * LottoResult : 로또 결과 화면으로 이동 (발표 전)
 * Back : 보관소(내 번호 Tab)로 이동 (발표 후)
 * Failed : 로직 진행 중 오류 발생한 경우 (이동 X, 오류 스낵바 표시)
 */
sealed interface RegisterNavigationType {
    data object None : RegisterNavigationType
    data object Back : RegisterNavigationType
    data class LottoResult(val data: List<String>) : RegisterNavigationType
}