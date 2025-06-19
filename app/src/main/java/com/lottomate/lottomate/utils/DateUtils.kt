package com.lottomate.lottomate.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateUtils {
    private const val DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd"
    private const val DEFAULT_TIME_ZONE = "Asia/Seoul"
    private const val DAYS_IN_WEEK = 7

    /**
     * 오늘로부터 다음 로또 추첨일까지의 일수를 계산합니다.
     *
     * @param day: 계산을 원하는 요일
     * ex) 토 : Calendar.SATURDAY
     */
    fun getDaysUntilNextDay(day: Int): Int {
        val today = Calendar.getInstance()

        // 오늘의 요일을 기준으로 다음 토요일을 계산
        val daysToSaturday = (day - today[Calendar.DAY_OF_WEEK] + 7) % 7
        today.add(Calendar.DAY_OF_YEAR, daysToSaturday)

        // 남은 일수 계산
        return daysToSaturday
    }

    /**
     * 추첨 시간까지 남은 시간을 계산합니다.
     *
     * 로또 : 8시 45분
     * 연금복권 : 7시 5분쯤
     */
    fun getHoursUntilTargetTime(targetHour: Int, targetMinute: Int): Long {
        val now = Calendar.getInstance()

        // 오늘 날짜가 토요일이라면 20시 45분까지 남은 시간을 계산
        if (now[Calendar.DAY_OF_WEEK] == Calendar.SATURDAY) {
            // 20시 45분으로 설정
            val targetTime = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, targetHour)
                set(Calendar.MINUTE, targetMinute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // 오늘 20시 45분까지 남은 시간 계산
            val diffMillis = targetTime.timeInMillis - now.timeInMillis
            return diffMillis / (1000 * 60 * 60) // 밀리초를 시간 단위로 변환
        }

        // 만약 오늘이 토요일이 아니면 0을 반환
        return 0
    }

    /**
     * Lotto 회차 날짜 계산 (7일 간격)
     *
     * @param lastRoundDate 최근 로또 당첨 회차 날짜
     * @param index 회차 피커에서 선택한 index
     * @isFuture 미래 날짜 계산 여부 (1주일 후에 대한 날짜를 계산하기 위하여 추가)
     *
     * @return 계산 완료한 날짜
     */
    fun calLottoRoundDate(lastRoundDate: String, index: Int, isFuture: Boolean = false): String {
        val (year, month, day) = lastRoundDate.split(".").map { it.toInt() }
        val date = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month.minus(1))
            set(Calendar.DAY_OF_MONTH, day)
            val offset = DAYS_IN_WEEK * index
            add(Calendar.DATE, if (isFuture) offset else -offset)
        }

        return buildString {
            append(date.get(Calendar.YEAR))
            append(".")
            append(String.format(Locale.KOREA, "%02d", date.get(Calendar.MONTH).plus(1)))
            append(".")
            append(String.format(Locale.KOREA, "%02d", date.get(Calendar.DAY_OF_MONTH)))
        }
    }

    /**
     * 현재 날짜 계산 함수
     *
     * @return yyyy-MM-dd 형식을 가진 현재 날짜
     */
    fun getCurrentDate(): String {
        val current = System.currentTimeMillis()
        val date = Date(current)
        val dateFormat = SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD, Locale.KOREA).apply {
            timeZone = TimeZone.getTimeZone(DEFAULT_TIME_ZONE)
        }

        return dateFormat.format(date)
    }

    /**
     * 오늘날짜로부터 과거인지 판별하는 함수
     *
     * @param pastDate
     */
    fun isDateInPast(date: String): Boolean {
        val (currentYear, currentMonth, currentDay) = getCurrentDate().split("-").map { it.toInt() }
        val (pastYear, pastMonth, pastDay) = date.split("-").map { it.toInt() }
        val calendar = Calendar.getInstance(TimeZone.getTimeZone(DEFAULT_TIME_ZONE))

        calendar.set(currentYear, currentMonth, currentDay)
        val today = getIgnoredTimeDays(calendar)

        calendar.set(pastYear, pastMonth, pastDay)
        val pastDate = getIgnoredTimeDays(calendar)

        return pastDate < today
    }

    fun isPrizeExpired(date: String): Boolean {
        val formatter = SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD, Locale.KOREA)
        val deadlineDate = formatter.parse(calculatePrizeDeadline(date)) ?: return false
        val currentDate = formatter.parse(getCurrentDate()) ?: return false

        return currentDate.after(deadlineDate)
    }

    private fun calculatePrizeDeadline(date: String): String {
        val formatter = SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD, Locale.KOREA)
        val originalDate = formatter.parse(date) ?: throw IllegalArgumentException("날짜 형식이 올바르지 않습니다")
        val calendar = Calendar.getInstance().apply {
            time = originalDate
            add(Calendar.YEAR, 1)
        }

        return formatter.format(calendar.time)
    }

    private fun getIgnoredTimeDays(calendar: Calendar): Long {
        return calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
}