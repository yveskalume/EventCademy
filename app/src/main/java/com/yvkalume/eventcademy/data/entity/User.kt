package com.yvkalume.eventcademy.data.entity

import androidx.annotation.Keep
import java.util.Date

@Keep
data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val photoUrl: String = "",
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val role: UserRole = UserRole.USER,
)
