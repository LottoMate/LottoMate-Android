package com.lottomate.lottomate.utils

import java.util.Calendar
import java.util.Locale

object DateUtils {
    private const val DAYS_IN_WEEK = 7

    /**
     * Lotto 회차 날짜 계산 (7일 간격)
     *
     * @param lastRoundDate 최근 로또 당첨 회차 날짜
     * @param index 회차 피커에서 선택한 index
     *
     * @return 계산 완료한 날짜
     */
    fun calLottoRoundDate(lastRoundDate: String, index: Int): String {
        val (year, month, day) = lastRoundDate.split(".").map { it.toInt() }
        val date = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month.minus(1))
            set(Calendar.DAY_OF_MONTH, day)
            add(Calendar.DATE, -(DAYS_IN_WEEK * index.minus(1)))
        }

        return buildString {
            append(date.get(Calendar.YEAR))
            append(".")
            append(String.format(Locale.KOREA, "%02d", date.get(Calendar.MONTH).plus(1)))
            append(".")
            append(String.format(Locale.KOREA, "%02d", date.get(Calendar.DAY_OF_MONTH)))
        }
    }
}