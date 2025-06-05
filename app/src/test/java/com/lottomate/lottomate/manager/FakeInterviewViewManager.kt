package com.lottomate.lottomate.manager

import com.lottomate.lottomate.data.manager.InterviewViewManager
import com.lottomate.lottomate.data.model.InterviewViewEntity
import com.lottomate.lottomate.utils.DateUtils

class FakeInterviewViewManager(
    private var today: String = DateUtils.getCurrentDate()
) : InterviewViewManager {
    private var viewedData = InterviewViewEntity(
        date = today,
        interviewIds = mutableSetOf()
    )

    override suspend fun addInterviewId(id: Int) {
        refreshIfDateChanged()
        viewedData = viewedData.copy(
            interviewIds = viewedData.interviewIds + id
        )
    }

    override suspend fun getUnviewedInterviewIds(latestInterviewId: Int): List<Int> {
        refreshIfDateChanged()

        val unviewed = (latestInterviewId downTo 1)
            .filterNot { viewedData.interviewIds.contains(it) }

        if (unviewed.isEmpty()) {
            viewedData = viewedData.copy(interviewIds = mutableSetOf())
        }

        return (latestInterviewId downTo 1)
            .filterNot { viewedData.interviewIds.contains(it) }
            .take(5)
    }

    fun getTodayViewedIds(): List<Int> {
        return viewedData.interviewIds.toList()
    }

    fun setDate(date: String) {
        today = date
    }

    private fun refreshIfDateChanged() {
        if (viewedData.date != today) {
            viewedData = InterviewViewEntity(today, mutableSetOf())
        }
    }
}