package com.yveskalume.eventcademy.core.domain.repository

import com.yveskalume.eventcademy.core.domain.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {

    fun getAllEventsStream(): Flow<List<Event>>
    fun getAllUpComingEventsStream(): Flow<List<Event>>

    fun getAllPastEventsStream(): Flow<List<Event>>

    suspend fun getEventByUid(eventUid: String): Event?

    fun getEventCreatedByCurrentUser(): Flow<List<Event>>

    suspend fun createEvent(event: Event)

    suspend fun deleteEvent(eventUid: String)
}