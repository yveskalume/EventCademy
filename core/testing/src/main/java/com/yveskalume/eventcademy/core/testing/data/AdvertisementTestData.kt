package com.yveskalume.eventcademy.core.testing.data

import com.yveskalume.eventcademy.core.domain.model.Advertisement
import com.yveskalume.eventcademy.core.domain.model.AdvertisementType
import java.util.Date

val advertisementTestData = listOf(
    Advertisement(
        uid = "ad123",
        title = "Summer Festival",
        description = "Join our exciting summer festival event!",
        imageUrl = "https://www.example.com/summer_festival.jpg",
        destination = "https://www.example.com/festival_page",
        userUid = "user123",
        published = true,
        type = AdvertisementType.INTERNAL_EVENT,
        featuredUntil = Date(System.currentTimeMillis() + 864000000), // Featured until 10 days from now
        createdAt = Date()
    ),
    Advertisement(
        uid = "ad456",
        title = "New Book Release",
        description = "Check out the latest book release from our partner author!",
        imageUrl = "https://www.example.com/book_release.jpg",
        destination = "https://www.example.com/book_page",
        userUid = "user456",
        published = true,
        type = AdvertisementType.EXTERNAL_CONTENT,
        featuredUntil = Date(System.currentTimeMillis() + 604800000), // Featured until 7 days from now
        createdAt = Date()
    ),
    Advertisement(
        uid = "ad789",
        title = "Fitness Workshop",
        description = "Register for our fitness workshop and learn about healthy habits.",
        imageUrl = "https://www.example.com/fitness_workshop.jpg",
        destination = "https://www.example.com/workshop_registration",
        userUid = "user789",
        published = true,
        type = AdvertisementType.INTERNAL_EVENT,
        featuredUntil = Date(System.currentTimeMillis() + 1209600000), // Featured until 14 days from now
        createdAt = Date()
    ),
    Advertisement(
        uid = "ad012",
        title = "Travel Deals",
        description = "Explore exciting travel deals for your next vacation!",
        imageUrl = "https://www.example.com/travel_deals.jpg",
        destination = "https://www.example.com/travel_deals_page",
        userUid = "user012",
        published = true,
        type = AdvertisementType.EXTERNAL_CONTENT,
        featuredUntil = Date(System.currentTimeMillis() + 518400000), // Featured until 6 days from now
        createdAt = Date()
    ),
    Advertisement(
        uid = "ad678",
        title = "Art Exhibition",
        description = "Visit our art exhibition featuring local artists' work.",
        imageUrl = "https://www.example.com/art_exhibition.jpg",
        destination = "https://www.example.com/exhibition_details",
        userUid = "user678",
        published = true,
        type = AdvertisementType.INTERNAL_EVENT,
        featuredUntil = Date(System.currentTimeMillis() + 259200000), // Featured until 3 days from now
        createdAt = Date()
    )
)