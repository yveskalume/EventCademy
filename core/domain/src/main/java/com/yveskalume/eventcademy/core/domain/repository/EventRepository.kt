package com.yveskalume.eventcademy.core.domain.repository

import com.yveskalume.eventcademy.core.domain.model.Event

interface EventRepository {

    fun getAllUpComingEventsStream(): kotlinx.coroutines.flow.Flow<List<Event>>

    suspend fun getEventByUid(eventUid: String): Event?

    fun getEventCreatedByCurrentUser(): kotlinx.coroutines.flow.Flow<List<Event>>

    suspend fun createEvent(event: Event)

    suspend fun deleteEvent(eventUid: String)
}