package com.yveskalume.eventcademy.core.domain.model

import java.util.Date

data class Post(
    val uid: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val eventUrl: String = "",
    val userUid: String = "",
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
)