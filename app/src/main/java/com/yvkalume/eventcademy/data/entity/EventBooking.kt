package com.yvkalume.eventcademy.data.entity

import java.util.Date

data class EventBooking(
    // the uid of the event is the userUid_eventUid
    val uid: String = "",
    val eventUid: String = "",
    val eventName: String = "",
    val eventDate: Date? = null,
    val eventImageUrl: String = "",
    val eventLocation: String = "",
    val userUid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val userProfileUrl: String = "",
    val createdAt: String = "",
)