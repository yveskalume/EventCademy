package com.yvkalume.eventcademy.data.util

import com.google.firebase.auth.FirebaseUser
import com.yvkalume.eventcademy.data.entity.User
import java.util.Date

fun FirebaseUser.toDomainUser() = User(
    uid = uid,
    name = displayName!!,
    email = email!!,
    photoUrl = photoUrl.toString(),
    createdAt = Date(),
    updatedAt = Date(),
)