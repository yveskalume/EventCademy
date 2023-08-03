package com.yvkalume.eventcademy.data.entity

import java.util.Date
import java.util.UUID

data class Advertisement(
    val uid: String = UUID.randomUUID().toString(),
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val destination: String = "",
    val userUid: String = "",
    val published: Boolean = false,
    val type: AdvertisementType = AdvertisementType.INTERNAL_EVENT,
    val featuredUntil: Date? = null,
    val createdAt: Date? = null,
)

// fake data list
val fakeAdvertisementList = listOf(
    Advertisement(
        title = "Event 1",
        description = "Description 1",
        imageUrl = "https://picsum.photos/200/300",
        destination = "https://picsum.photos/200/300",
        userUid = "1",
        published = true,
        type = AdvertisementType.INTERNAL_EVENT,
        featuredUntil = Date(),
        createdAt = Date()
    ),
    Advertisement(
        title = "Event 2",
        description = "Description 2",
        imageUrl = "https://picsum.photos/200/300",
        destination = "https://picsum.photos/200/300",
        userUid = "1",
        published = true,
        type = AdvertisementType.EXTERNAL_CONTENT,
        featuredUntil = Date(),
        createdAt = Date()
    ),
    Advertisement(
        title = "Event 3",
        description = "Description 3",
        imageUrl = "https://picsum.photos/200/300",
        destination = "https://picsum.photos/200/300",
        userUid = "1",
        published = true,
        type = AdvertisementType.INTERNAL_EVENT,
        featuredUntil = Date(),
        createdAt = Date()
    ),
    Advertisement(
        title = "Event 4",
        description = "Description 4",
        imageUrl = "https://picsum.photos/200/300",
        destination = "https://picsum.photos/200/300",
        userUid = "1",
        published = true,
        type = AdvertisementType.EXTERNAL_CONTENT,
        featuredUntil = Date(),
        createdAt = Date()
    ),
    Advertisement(
        title = "Event 5",
        description = "Description 5",
        imageUrl = "https://picsum.photos/200/300",
        destination = "https://picsum.photos/200/300",
        userUid = "1",
        published = true,
        type = AdvertisementType.INTERNAL_EVENT,
        featuredUntil = Date(),
        createdAt = Date()
    ),
)
