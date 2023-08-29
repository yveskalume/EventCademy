package com.yveskalume.eventcademy.core.testing.repository

import com.yveskalume.eventcademy.core.domain.model.Advertisement
import com.yveskalume.eventcademy.core.domain.repository.AdvertisementRepository
import com.yveskalume.eventcademy.core.testing.data.advertisementTestData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeAdvertisementRepository : AdvertisementRepository {
    override fun getAllAdvertisementsStream(): Flow<List<Advertisement>> {
        return flow {
            emit(advertisementTestData)
        }
    }
}