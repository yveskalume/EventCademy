package com.yveskalume.eventcademy.core.data.firebase.util

import com.google.firebase.auth.FirebaseUser
import com.yveskalume.eventcademy.core.domain.model.User
import java.util.Date

fun FirebaseUser.toDomainUser() = User(
    uid = uid,
    name = displayName!!,
    email = email!!,
    photoUrl = photoUrl.toString(),
    createdAt = Date(),
    updatedAt = Date(),
)