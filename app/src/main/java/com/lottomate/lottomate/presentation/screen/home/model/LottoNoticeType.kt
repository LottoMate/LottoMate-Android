package com.lottomate.lottomate.presentation.screen.home.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.lottomate.lottomate.R

/**
 * 홈 화면의 상단에 보여지는 로또 당첨 알림 배너 내용입니다.
 */
enum class LottoNoticeType(
    @StringRes val message: Int,
    @StringRes val keyword: Int,
    @DrawableRes val icon: Int,
) {
    LOTTO_645_NOTICE(
        message = R.string.home_top_notice_lotto645_1,
        keyword = R.string.home_top_notice_lotto645_1_keyword,
        icon = R.drawable.icon_lotto645_rank_first,
    ),
    LOTTO_645_COUNTDOWN(
        message = R.string.home_top_notice_lotto645_3,
        keyword = R.string.home_top_notice_lotto645_3_keyword,
        icon = R.drawable.icon_lotto645_rank_first,
    ),
    LOTTO_720_NOTICE(
        message = R.string.home_top_notice_lotto720_1,
        keyword = R.string.home_top_notice_lotto720_1_keyword,
        icon = R.drawable.icon_lotto720_rank_first,
    ),
    LOTTO_720_COUNTDOWN(
        message = R.string.home_top_notice_lotto720_3,
        keyword = R.string.home_top_notice_lotto720_3_keyword,
        icon = R.drawable.icon_lotto720_rank_first,
    );

    companion object {
        fun findByIndex(index: Int): LottoNoticeType {
            return entries[index]
        }
    }
}