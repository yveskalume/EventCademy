package com.yveskalume.eventcademy.core.domain.repository

import com.yveskalume.eventcademy.core.domain.model.Advertisement
import kotlinx.coroutines.flow.Flow

interface AdvertisementRepository {

    fun getAllAdvertisementsStream(): Flow<List<Advertisement>>
}