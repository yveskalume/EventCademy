package com.yveskalume.eventcademy.core.domain.model

data class PostLike(
    val uid: String = "",
    val postUid: String = "",
    val postImageUrl: List<String> =emptyList(),
    val userUid: String = "",
    val userName: String = "",
    val email: String = "",
    val userPhotoUrl: String = "",
    val createdAt: String = "",
)
