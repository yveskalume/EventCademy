package com.yveskalume.eventcademy.core.domain.model

import java.util.Date

data class Post(
    val uid: String = "",
    val postContent: String = "",
    val eventName: String? = null,
    val imageUrls: List<String> = emptyList(),
    val userUid: String = "",
    val createdAt: Date? = null,
    val updatedAt: Date? = null,
    val published: Boolean = false,
    val rejected: Boolean = false
)

val Post.statutText: String
    get() {
        return if (rejected){
            "Non publié"
        } else if (published){
            "Publié"
        } else{
            "En attente"
        }
    }

