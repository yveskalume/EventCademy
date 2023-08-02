package com.yvkalume.eventcademy.data.entity

import java.util.Date

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val photoUrl: String = "",
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val isOrganizer: Boolean = false,
)
