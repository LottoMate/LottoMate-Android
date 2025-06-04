package com.lottomate.lottomate.manager

import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class InterviewViewManagerTest {
    private lateinit var manager: FakeInterviewViewManager

    @Before
    fun setUp() {
        manager = FakeInterviewViewManager()
    }

    @Test
    fun `다른 날짜가 되면 초기화되어야 한다`() = runTest {
        manager.addInterviewId(5)
        manager.setDate("2025-05-05")

        // When : 다른 날짜가 되면
        val result = manager.getTodayViewedIds()

        // Then : 초기화되어야 한다
        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun `최신 인터뷰를 기준으로 보지않은 인터뷰 Id 5개를 반환해야 한다`() = runTest {
        listOf(1, 5, 7).forEach {
            manager.addInterviewId(it)
        }

        val result = manager.getUnviewedInterviewIds(10)

        Assert.assertEquals(listOf(10, 9, 8, 6, 4), result)
    }

    @Test
    fun `모든 인터뷰를 보았을 경우, 최신 인터뷰 5개를 반환해야 한다`() = runTest {
        (1..10).forEach { manager.addInterviewId(it) }

        val result = manager.getUnviewedInterviewIds(10)

        Assert.assertEquals(listOf(10, 9, 8, 7, 6), result)
    }

    @Test
    fun `조회 가능한 인터뷰가 5개 미만일 경우 조회 가능한 인터뷰를 반환해야 한다`() = runTest {
        (1..3).forEach { manager.addInterviewId(it) }

        val result = manager.getUnviewedInterviewIds(5)

        Assert.assertEquals(listOf(5, 4), result)
    }

    @Test
    fun `인터뷰를 조회하지 않았을 경우 최신 인터뷰 5개를 반환해야 한다`() = runTest {
        val result = manager.getUnviewedInterviewIds(10)

        Assert.assertEquals(listOf(10, 9, 8, 7, 6), result)
    }

    @Test
    fun `날짜가 변경되어 초기화된 직후에도 최신 인터뷰 5개를 반환해야 한다`() = runTest {
        manager.addInterviewId(8)
        manager.setDate("2025-06-10")

        val result = manager.getUnviewedInterviewIds(10)

        Assert.assertEquals(listOf(10, 9, 8, 7, 6), result)
    }
}