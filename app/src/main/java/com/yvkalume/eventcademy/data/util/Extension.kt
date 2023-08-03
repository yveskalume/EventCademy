package com.yvkalume.eventcademy.data.util

import com.google.firebase.auth.FirebaseUser
import com.yvkalume.eventcademy.data.entity.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun FirebaseUser.toDomainUser() = User(
    uid = uid,
    name = displayName!!,
    email = email!!,
    photoUrl = photoUrl.toString(),
    createdAt = Date(),
    updatedAt = Date(),
)

val Date.dayOfMonth: String
    get() = SimpleDateFormat("dd", Locale.FRENCH).format(this)

val Date.monthName: String
    get() = SimpleDateFormat("MMM", Locale.FRENCH).format(this)

val Date.readableDate: String
    get() = SimpleDateFormat("dd MMM yyyy", Locale.FRENCH).format(this)

val Date.DateAndTime: String
    get() = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.FRENCH).format(this)

val Date.time: String
    get() = SimpleDateFormat("HH:mm", Locale.FRENCH).format(this)