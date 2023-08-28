package com.yveskalume.eventcademy.core.domain.repository

import com.google.firebase.auth.AuthCredential
import com.yveskalume.eventcademy.core.domain.model.User

interface UserRepository {

    suspend fun signInWithCredential(credential: AuthCredential)

    suspend fun getCurrentUser(): User

    fun getCurrentUserStream(): kotlinx.coroutines.flow.Flow<User>
    suspend fun getUserByUid(userUid: String) : User?
}