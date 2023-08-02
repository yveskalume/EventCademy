package com.yvkalume.eventcademy.data.entity

data class User(
    val uid: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val profileUrl: String = "",
    val createdAt: String = "",
    val updatedAt: String = "",
    val isOrganizer: Boolean = false,
)
