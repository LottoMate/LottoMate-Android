package com.lottomate.lottomate.utils

import android.icu.text.DecimalFormat

object StringUtils {
    /**
     * 로또 당첨금을 "약 X억원" 또는 "X만원" 형식의 문자열로 변환하는 함수.
     *
     * - 금액이 1억 원 이상이면 "약 X억원" 형태로 변환 (만원 단위 생략)
     * - 금액이 1억 원 미만이면 "X만원" 형태로 변환
     *
     * @param amount 당첨금 금액 (원 단위)
     * @return 변환된 당첨금 문자열
     */
    fun formatLottoPrize(amount: Long): String {
        return if (amount >= 100_000_000) {
            "약 ${formatNumberWithCommas(amount / 100_000_000)}억 원"
        } else {
            val price = amount / 10_000

            if (price.toInt() == 5) "${price}만 원"
            else {
                "약 ${formatNumberWithCommas(amount / 10_000)}만 원"
            }
        }
    }

    /**
     * 숫자를 3자리마다 콤마(,)를 추가하여 문자열로 변환하는 함수.
     *
     * - 예: 1000000 -> "1,000,000"
     *
     * @param num 변환할 숫자 (Long 타입)
     * @return 콤마가 추가된 숫자 문자열
     */
    fun formatNumberWithCommas(num: Long): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(num)
    }

    /**
     * 숫자를 3자리마다 콤마(,)를 추가하여 문자열로 변환하는 함수.
     *
     * - 예: 1000000 -> "1,000,000"
     *
     * @param num 변환할 숫자 (Int 타입)
     * @return 콤마가 추가된 숫자 문자열
     */
    fun formatNumberWithCommas(num: Int): String {
        val formatter = DecimalFormat("#,###")
        return formatter.format(num)
    }
}