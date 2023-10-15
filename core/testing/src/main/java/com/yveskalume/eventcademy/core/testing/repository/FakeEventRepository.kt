package com.yveskalume.eventcademy.core.testing.repository

import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.repository.EventRepository
import com.yveskalume.eventcademy.core.testing.data.eventTestData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeEventRepository : EventRepository {
    override fun getAllEventsStream(): Flow<List<Event>> {
        return flow {
            emit(eventTestData)
        }
    }

    override fun getAllUpComingEventsStream(): Flow<List<Event>> {
        return flow {
            emit(eventTestData)
        }
    }

    override fun getAllPastEventsStream(): Flow<List<Event>> {
        return flow {
            emit(eventTestData)
        }
    }

    override suspend fun getEventByUid(eventUid: String): Event {
        return eventTestData.first().copy(uid = eventUid)
    }

    override fun getEventCreatedByCurrentUser(): Flow<List<Event>> {
        return flow {
            emit(eventTestData)
        }
    }

    override suspend fun createEvent(event: Event) {
        eventTestData.add(event)
    }

    override suspend fun deleteEvent(eventUid: String) {
        eventTestData.removeIf { it.uid == eventUid }
    }
}