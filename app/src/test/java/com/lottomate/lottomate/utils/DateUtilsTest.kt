package com.lottomate.lottomate.utils

import org.junit.Assert
import org.junit.Test

class DateUtilsTest {
    @Test
    fun `지급기한이 지난 복권 추첨 날짜를 입력하면, true를 반환한다`() {
        val lotteryWinDate = "2024-04-27"

        // When
        val isExpired = DateUtils.isPrizeExpired(lotteryWinDate)

        // Then
        Assert.assertTrue(isExpired)
    }

    @Test
    fun `지급기한이 지나지 않은 복권 추첨 날짜를 입력하면, false를 반환한다`() {
        val lotteryWinDate = "2025-03-24"

        // When
        val isExpired = DateUtils.isPrizeExpired(lotteryWinDate)

        // Then
        Assert.assertFalse(isExpired)
    }
}