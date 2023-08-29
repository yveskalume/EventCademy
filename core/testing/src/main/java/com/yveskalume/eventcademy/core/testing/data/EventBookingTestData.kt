package com.yveskalume.eventcademy.core.testing.data

import com.yveskalume.eventcademy.core.domain.model.EventBooking
import java.util.Date

val eventBookingTestData = mutableListOf(
    EventBooking(
        uid = "user123-event456",
        eventUid = "event456",
        eventName = "Summer Music Festival",
        eventDate = Date(),
        eventImageUrl = "https://www.example.com/summer_music.jpg",
        eventLocation = "Central Park, New York",
        userUid = "user123",
        userName = "Alice Johnson",
        email = "alice@example.com",
        userPhotoUrl = "https://www.example.com/alice_profile.jpg",
        createdAt = "2021-01-01 12:00:00"
    ),
    EventBooking(
        uid = "user789-event012",
        eventUid = "event012",
        eventName = "Tech Conference 2023",
        eventDate = Date(),
        eventImageUrl = "https://www.example.com/tech_conference.jpg",
        eventLocation = "Convention Center, San Francisco",
        userUid = "user789",
        userName = "Bob Smith",
        email = "bob@example.com",
        userPhotoUrl = "https://www.example.com/bob_profile.jpg",
        createdAt = "2021-02-15 10:30:00"
    ),
    EventBooking(
        uid = "user456-event789",
        eventUid = "event789",
        eventName = "Art Exhibition: Modern Masters",
        eventDate = Date(),
        eventImageUrl = "https://www.example.com/art_exhibition.jpg",
        eventLocation = "Art Gallery, London",
        userUid = "user456",
        userName = "Eva Williams",
        email = "eva@example.com",
        userPhotoUrl = "https://www.example.com/eva_profile.jpg",
        createdAt = "2021-03-20 14:15:00"
    ),
    EventBooking(
        uid = "user012-event345",
        eventUid = "event345",
        eventName = "Fitness Workshop: Mind and Body",
        eventDate = Date(),
        eventImageUrl = "https://www.example.com/fitness_workshop.jpg",
        eventLocation = "Wellness Center, Los Angeles",
        userUid = "user012",
        userName = "David Miller",
        email = "david@example.com",
        userPhotoUrl = "https://www.example.com/david_profile.jpg",
        createdAt = "2021-04-10 08:20:00"
    ),
    EventBooking(
        uid = "user678-event901",
        eventUid = "event901",
        eventName = "Food Tasting: Global Flavors",
        eventDate = Date(),
        eventImageUrl = "https://www.example.com/food_tasting.jpg",
        eventLocation = "Food Hall, Paris",
        userUid = "user678",
        userName = "Sophie Brown",
        email = "sophie@example.com",
        userPhotoUrl = "https://www.example.com/sophie_profile.jpg",
        createdAt = "2021-05-05 16:40:00"
    )
)