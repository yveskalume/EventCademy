package com.yveskalume.eventcademy.core.testing.repository

import com.yveskalume.eventcademy.core.domain.model.Post
import com.yveskalume.eventcademy.core.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FailingFakePostRepository: PostRepository {
    override suspend fun createPost(post: Post) {
        throw Exception("An error occurred")
    }

    override suspend fun deletePost(postUid: String) {
        throw Exception("An error occurred")
    }

    override suspend fun getPostByUid(postUid: String): Post? {
        throw Exception("An error occurred")
    }

    override fun getAllPosts(): Flow<List<Post>> {
        return flow {
            throw Exception("An error occurred")
        }
    }
}