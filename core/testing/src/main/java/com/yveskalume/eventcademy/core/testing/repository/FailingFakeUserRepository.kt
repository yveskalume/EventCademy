package com.yveskalume.eventcademy.core.testing.repository

import com.google.firebase.auth.AuthCredential
import com.yveskalume.eventcademy.core.domain.model.User
import com.yveskalume.eventcademy.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FailingFakeUserRepository : UserRepository {
    override suspend fun signInWithCredential(credential: AuthCredential) {
        throw Exception("An error occurred")
    }

    override suspend fun getCurrentUser(): User {
        throw Exception("An error occurred")
    }

    override fun getCurrentUserStream(): Flow<User> {
        return flow {
            throw Exception("An error occurred")
        }
    }

    override suspend fun getUserByUid(userUid: String): User? {
        throw Exception("An error occurred")
    }
}