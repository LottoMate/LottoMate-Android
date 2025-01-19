package com.lottomate.lottomate.presentation.screen.home.model

import androidx.annotation.DrawableRes
import com.lottomate.lottomate.R

/**
 * 홈 화면의 상단에 보여지는 로또 당첨 알림 배너 내용입니다.
 */
enum class LottoNoticeType(val text: String, @DrawableRes val icon: Int) {
    LOTTO_645_NOTICE(
        text = "로또는 매주 토요일 오후 8시 45분에 추첨해요.",
        icon = R.drawable.icon_lotto645_rank_first,
    ),
    LOTTO_645_COUNTDOWN(
        text = "로또 당첨 발표는 오늘이에요!",
        icon = R.drawable.icon_lotto645_rank_first,
    ),
    LOTTO_720_NOTICE(
        text = "연금복권은 매주 목요일 오후 7시 5분쯤 추첨해요.",
        icon = R.drawable.icon_lotto720_rank_first,
    ),
    LOTTO_720_COUNTDOWN(
        text = "연금복권 당첨 발표는 오늘이에요!",
        icon = R.drawable.icon_lotto720_rank_first,
    );

    companion object {
        fun findByIndex(index: Int): LottoNoticeType {
            return entries[index]
        }
    }
}