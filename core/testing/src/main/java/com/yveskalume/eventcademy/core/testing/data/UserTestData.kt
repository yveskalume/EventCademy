package com.yveskalume.eventcademy.core.testing.data

import com.yveskalume.eventcademy.core.domain.model.User
import com.yveskalume.eventcademy.core.domain.model.UserRole
import java.util.Date

val userTestData = listOf(
    User(
        uid = "user123",
        email = "alice@example.com",
        name = "Alice Johnson",
        photoUrl = "https://www.example.com/alice_profile.jpg",
        createdAt = Date(),
        updatedAt = Date(),
        role = UserRole.USER
    ),
    User(
        uid = "user456",
        email = "bob@example.com",
        name = "Bob Smith",
        photoUrl = "https://www.example.com/bob_profile.jpg",
        createdAt = Date(),
        updatedAt = Date(),
        role = UserRole.ORGANIZER
    ),
    User(
        uid = "user012",
        email = "david@example.com",
        name = "David Miller",
        photoUrl = "https://www.example.com/david_profile.jpg",
        createdAt = Date(),
        updatedAt = Date(),
        role = UserRole.ADMIN
    ),
)