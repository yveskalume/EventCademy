package com.yveskalume.eventcademy.core.domain.repository

import com.yveskalume.eventcademy.core.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun createPost(post: Post)

    suspend fun deletePost(postUid: String)

    suspend fun getPostByUid(postUid: String): Post?

    fun getAllPosts(): Flow<List<Post>>

}