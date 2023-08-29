package com.yveskalume.eventcademy.core.testing.repository

import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FailingFakeEventRepository : EventRepository {
    override fun getAllUpComingEventsStream(): Flow<List<Event>> {
        return flow {
            throw Exception("An error occurred")
        }
    }

    override suspend fun getEventByUid(eventUid: String): Event? {
        throw Exception("An error occurred")
    }

    override fun getEventCreatedByCurrentUser(): Flow<List<Event>> {
        return flow {
            throw Exception("An error occurred")
        }
    }

    override suspend fun createEvent(event: Event) {
        throw Exception("An error occurred")
    }

    override suspend fun deleteEvent(eventUid: String) {
        throw Exception("An error occurred")
    }
}