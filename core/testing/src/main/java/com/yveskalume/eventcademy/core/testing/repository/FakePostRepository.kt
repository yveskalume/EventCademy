package com.yveskalume.eventcademy.core.testing.repository

import com.yveskalume.eventcademy.core.domain.model.Post
import com.yveskalume.eventcademy.core.domain.repository.PostRepository
import com.yveskalume.eventcademy.core.testing.data.postTestData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePostRepository: PostRepository {
    override suspend fun createPost(post: Post) {
        postTestData.add(post)
    }

    override suspend fun deletePost(postUid: String) {
        postTestData.removeIf{ it.uid == postUid }
    }

    override suspend fun getPostByUid(postUid: String): Post? {
        return postTestData.first().copy(uid = postUid)
    }

    override fun getAllPosts(): Flow<List<Post>> {
        return flow {
            emit(postTestData)
        }
    }

}