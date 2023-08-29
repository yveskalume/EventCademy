package com.yveskalume.eventcademy.core.testing.repository

import com.yveskalume.eventcademy.core.domain.model.Advertisement
import com.yveskalume.eventcademy.core.domain.repository.AdvertisementRepository

class FailingFakeAdvertisementRepository : AdvertisementRepository {
    override fun getAllAdvertisementsStream(): kotlinx.coroutines.flow.Flow<List<Advertisement>> {
        return kotlinx.coroutines.flow.flow {
            throw Exception("An error occurred")
        }
    }
}