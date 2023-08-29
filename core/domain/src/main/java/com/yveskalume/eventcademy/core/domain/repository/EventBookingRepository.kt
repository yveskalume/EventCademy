package com.yveskalume.eventcademy.core.domain.repository

import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.model.EventBooking
import kotlinx.coroutines.flow.Flow

interface EventBookingRepository {

    fun getAllBookingByEventUid(eventUid: String): Flow<List<EventBooking>>

    fun getAllUserEventBookings(): Flow<List<EventBooking>>

    suspend fun checkIfUserHasBooked(eventUid: String): Flow<Boolean>

    suspend fun createBooking(event: Event)

    suspend fun deleteBooking(eventUid: String)
}