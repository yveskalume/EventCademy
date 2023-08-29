package com.yveskalume.eventcademy.core.testing.repository

import com.google.firebase.auth.AuthCredential
import com.yveskalume.eventcademy.core.domain.model.User
import com.yveskalume.eventcademy.core.domain.repository.UserRepository
import com.yveskalume.eventcademy.core.testing.data.userTestData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserRepository : UserRepository {
    override suspend fun signInWithCredential(credential: AuthCredential) {
        return
    }

    override suspend fun getCurrentUser(): User {
        return userTestData.first()
    }

    override fun getCurrentUserStream(): Flow<User> {
        return flow {
            emit(userTestData.first())
        }
    }

    override suspend fun getUserByUid(userUid: String): User? {
        return userTestData.first().copy(uid = userUid)
    }

}