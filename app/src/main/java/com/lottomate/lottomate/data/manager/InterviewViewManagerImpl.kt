package com.lottomate.lottomate.data.manager

import com.lottomate.lottomate.data.datastore.LottoMateDataStore
import com.lottomate.lottomate.data.model.InterviewViewEntity
import com.lottomate.lottomate.utils.DateUtils
import javax.inject.Inject

class InterviewViewManagerImpl @Inject constructor(

) : InterviewViewManager {
    override suspend fun addInterviewId(id: Int) {
        val current = LottoMateDataStore.getInterviewViewed()

        val update = current.copy(interviewIds = current.interviewIds + id)
        LottoMateDataStore.saveInterviewViewed(update)
    }

    override suspend fun getUnviewedInterviewIds(latestInterviewId: Int): List<Int> {
        var current = LottoMateDataStore.getInterviewViewed()

        val unviewed = (latestInterviewId downTo 1)
            .filterNot { current.interviewIds.contains(it) }

        if (unviewed.isEmpty()) {
            current = InterviewViewEntity(DateUtils.getCurrentDate(), emptySet())
            LottoMateDataStore.saveInterviewViewed(current)
        }

        return (latestInterviewId downTo 1)
            .filterNot { current.interviewIds.contains(it) }
            .take(DEFAULT_COUNT)
    }

    companion object {
        private const val DEFAULT_COUNT = 5
    }
}