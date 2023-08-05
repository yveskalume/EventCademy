package com.yveskalume.eventcademy.data.entity

import androidx.annotation.Keep
import java.util.Date

@Keep
data class Event(
    val uid: String = "",
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
