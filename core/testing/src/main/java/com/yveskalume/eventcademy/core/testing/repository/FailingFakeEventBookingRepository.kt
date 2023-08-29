package com.yveskalume.eventcademy.core.testing.repository

import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.model.EventBooking
import com.yveskalume.eventcademy.core.domain.repository.EventBookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FailingFakeEventBookingRepository : EventBookingRepository {
    override fun getAllBookingByEventUid(eventUid: String): Flow<List<EventBooking>> {
        return flow {
            throw Exception("An error occurred")
        }
    }

    override fun getAllUserEventBookings(): Flow<List<EventBooking>> {
        return flow {
            throw Exception("An error occurred")
        }
    }

    override suspend fun checkIfUserHasBooked(eventUid: String): Flow<Boolean> {
        return flow {
            throw Exception("An error occurred")
        }
    }

    override suspend fun createBooking(event: Event) {
        throw Exception("An error occurred")
    }

    override suspend fun deleteBooking(eventUid: String) {
        throw Exception("An error occurred")
    }
}