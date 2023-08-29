package com.yveskalume.eventcademy.core.testing.repository

import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.model.EventBooking
import com.yveskalume.eventcademy.core.domain.repository.EventBookingRepository
import com.yveskalume.eventcademy.core.testing.data.eventBookingTestData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

class FakeEventBookingRepository : EventBookingRepository {
    override fun getAllBookingByEventUid(eventUid: String): Flow<List<EventBooking>> {
        return flow {
            emit(eventBookingTestData)
        }
    }

    override fun getAllUserEventBookings(): Flow<List<EventBooking>> {
        return flow {
            emit(eventBookingTestData)
        }
    }

    override suspend fun checkIfUserHasBooked(eventUid: String): Flow<Boolean> {
        return flow {
            emit(true)
        }
    }

    override suspend fun createBooking(event: Event) {
        eventBookingTestData.add(
            EventBooking(
                uid = "userUid-${event.uid}",
                eventUid = event.uid,
                eventName = event.name,
                eventDate = event.startDate,
                eventImageUrl = event.imageUrl,
                eventLocation = event.location,
                userUid = "userUid",
                userName = "John Doe",
                email = "john@gmail.com",
                userPhotoUrl = "",
                createdAt = Date().toString()
            )
        )
    }

    override suspend fun deleteBooking(eventUid: String) {
        eventBookingTestData.removeIf { it.eventUid == eventUid }
    }
}