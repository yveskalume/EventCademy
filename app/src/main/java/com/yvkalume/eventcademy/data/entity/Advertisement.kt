package com.yvkalume.eventcademy.data.entity

import java.util.Date

data class Advertisement(
    val uid: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val eventUrl: String = "",
    val userUid: String = "",
    val isPublished: Boolean = false,
    val type: AdvertisementType = AdvertisementType.INTERNAL_EVENT,
    val featuredUntil: Date? = null,
    val createdAt: Date? = null,
)

// fake data list
val fakeAdvertisementList = listOf(
    Advertisement(
        uid = "1",
        title = "Event 1",
        description = "Description 1",
        imageUrl = "https://picsum.photos/200/300",
        eventUrl = "https://picsum.photos/200/300",
        userUid = "1",
        isPublished = true,
        type = AdvertisementType.INTERNAL_EVENT,
        featuredUntil = Date(),
        createdAt = Date()
    ),
    Advertisement(
        uid = "2",
        title = "Event 2",
        description = "Description 2",
        imageUrl = "https://picsum.photos/200/300",
        eventUrl = "https://picsum.photos/200/300",
        userUid = "1",
        isPublished = true,
        type = AdvertisementType.EXTERNAL_CONTENT,
        featuredUntil = Date(),
        createdAt = Date()
    ),
    Advertisement(
        uid = "3",
        title = "Event 3",
        description = "Description 3",
        imageUrl = "https://picsum.photos/200/300",
        eventUrl = "https://picsum.photos/200/300",
        userUid = "1",
        isPublished = true,
        type = AdvertisementType.INTERNAL_EVENT,
        featuredUntil = Date(),
        createdAt = Date()
    ),
    Advertisement(
        uid = "4",
        title = "Event 4",
        description = "Description 4",
        imageUrl = "https://picsum.photos/200/300",
        eventUrl = "https://picsum.photos/200/300",
        userUid = "1",
        isPublished = true,
        type = AdvertisementType.EXTERNAL_CONTENT,
        featuredUntil = Date(),
        createdAt = Date()
    ),
    Advertisement(
        uid = "5",
        title = "Event 5",
        description = "Description 5",
        imageUrl = "https://picsum.photos/200/300",
        eventUrl = "https://picsum.photos/200/300",
        userUid = "1",
        isPublished = true,
        type = AdvertisementType.INTERNAL_EVENT,
        featuredUntil = Date(),
        createdAt = Date()
    ),
)
