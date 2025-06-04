package com.lottomate.lottomate.data.manager

interface InterviewViewManager {
    suspend fun addInterviewId(id: Int)
    suspend fun getUnviewedInterviewIds(latestInterviewId: Int): List<Int>
}