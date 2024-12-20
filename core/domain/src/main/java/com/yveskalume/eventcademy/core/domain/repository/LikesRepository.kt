package com.yveskalume.eventcademy.core.domain.repository

import com.yveskalume.eventcademy.core.domain.model.Post
import com.yveskalume.eventcademy.core.domain.model.PostLike
import kotlinx.coroutines.flow.Flow

interface PostLikesRepository {
    fun getAllLikesByPostUid(postUid: String): Flow<List<PostLike>>

    fun getAllUserPostLikes(): Flow<List<PostLike>>

    suspend fun checkIfUserHasLiked(postUid: String): Flow<Boolean>

    suspend fun createLike(post: Post)

    suspend fun deleteLike(postUid: String)
}