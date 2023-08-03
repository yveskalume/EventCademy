package com.yvkalume.eventcademy.data.entity

import java.util.Date
import java.util.UUID

data class Event(
    val uid: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val location: String = "",
    val price: Double = 0.0,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val imageUrl: String = "",
    val eventUrl: String = "",
    val type: EventType = EventType.HYBRID,
    val userUid: String = "",
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val published: Boolean = false,
)

val fakeEvents = listOf<Event>(
    Event(
        name = "Event 1",
        description = "Description 1",
        location = "Location 1",
        price = 0.0,
        startDate = Date(),
        endDate = Date(),
        imageUrl = "https://picsum.photos/200/300",
        eventUrl = "https://picsum.photos/200/300",
        type = EventType.HYBRID,
        userUid = "userUid",
        createdAt = Date(),
        updatedAt = Date(),
        published = true
    ),
    Event(
        name = "Event 2",
        description = "Description 2",
        location = "Location 2",
        price = 0.0,
        startDate = Date(),
        endDate = Date(),
        imageUrl = "https://picsum.photos/200/300",
        eventUrl = "https://picsum.photos/200/300",
        type = EventType.HYBRID,
        userUid = "userUid",
        createdAt = Date(),
        updatedAt = Date(),
        published = true
    ),
    Event(
        name = "Event 3",
        description = "Description 3",
        location = "Location 3",
        price = 0.0,
        startDate = Date(),
        endDate = Date(),
        imageUrl = "https://picsum.photos/200/300",
        eventUrl = "https://picsum.photos/200/300",
        type = EventType.HYBRID,
        userUid = "userUid",
        createdAt = Date(),
        updatedAt = Date(),
        published = true
    ),
    Event(
        name = "Event 4",
        description = "Description 4",
        location = "Location 4",
        price = 0.0,
        startDate = Date(),
        endDate = Date(),
        imageUrl = "https://picsum.photos/200/300",
        eventUrl = "https://picsum.photos/200/300",
        type = EventType.HYBRID,
        userUid = "userUid",
        createdAt = Date(),
        updatedAt = Date(),
        published = true
    ),
    Event(
        name = "Event 5",
        description = "Description 5",
        location = "Location 5",
        price = 0.0,
        startDate = Date(),
        endDate = Date(),
        imageUrl = "https://picsum.photos/200/300",
        eventUrl = "https://picsum.photos/200/300",
    ),
)

