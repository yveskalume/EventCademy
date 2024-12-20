package com.yveskalume.eventcademy.core.testing.data

import com.yveskalume.eventcademy.core.domain.model.EventType
import com.yveskalume.eventcademy.core.domain.model.Post
import java.util.Date

val postTestData = mutableListOf(
    Post(
        uid = "1",
        eventName = "Post 1",
        postContent = "Description 1",
        userUid = "1",
        imageUrls = listOf(
            "https://www.example.com/summer_festival.jpg",
            "https://www.example.com/book_release.jpg",
            "https://www.example.com/fitness_workshop.jpg",
            "https://www.example.com/travel_deals.jpg"
        ),
        createdAt = Date(),
        updatedAt = Date(),
    ),
    Post(
        uid = "2",
        eventName = "Post 2",
        postContent = "Description 2",
        userUid = "2",
        imageUrls = listOf(
            "https://www.example.com/summer_festival.jpg",
            "https://www.example.com/book_release.jpg",
            "https://www.example.com/fitness_workshop.jpg",
            "https://www.example.com/travel_deals.jpg"
        ),
        createdAt = Date(),
        updatedAt = Date(),
    ),
    Post(
        uid = "3",
        eventName = "Post 3",
        postContent = "Description 3",
        userUid = "3",
        imageUrls = listOf(
            "https://www.example.com/summer_festival.jpg",
            "https://www.example.com/book_release.jpg",
            "https://www.example.com/fitness_workshop.jpg",
            "https://www.example.com/travel_deals.jpg"
        ),
        createdAt = Date(),
        updatedAt = Date(),
    )
)