package com.yveskalume.eventcademy.core.data.firebase.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.yveskalume.eventcademy.core.domain.model.User
import com.yveskalume.eventcademy.core.data.firebase.util.FirestoreCollections
import com.yveskalume.eventcademy.core.data.firebase.util.toDomainUser
import com.yveskalume.eventcademy.core.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRepository {

    private suspend fun saveUserToFirestore(firebaseUser: FirebaseUser?) {
        withContext(Dispatchers.IO) {
            val user = firebaseUser?.toDomainUser()
            if (user != null) {
                firestore.collection(FirestoreCollections.USERS).document(user.uid).set(user)
                    .await()
            }
        }
    }

    override suspend fun signInWithCredential(credential: AuthCredential) {
        withContext(Dispatchers.IO) {
            try {
                val user = firebaseAuth.signInWithCredential(credential).await().user
                saveUserToFirestore(user)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    override suspend fun getCurrentUser(): User {
        return withContext(Dispatchers.IO) {
            val uid = firebaseAuth.uid
            val task = firestore.document("${FirestoreCollections.USERS}/$uid").get()
            return@withContext task.await().toObject(User::class.java)!!
        }
    }

    override fun getCurrentUserStream() = callbackFlow {
        val uid = firebaseAuth.uid
        val listener = firestore.document("${FirestoreCollections.USERS}/$uid")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    close(error)
                    return@addSnapshotListener
                }
                val user = value.toObject(User::class.java)
                if (user != null) {
                    trySend(user)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getUserByUid(userUid: String): User? {
        return withContext(Dispatchers.IO) {
            val task = firestore.document("${FirestoreCollections.USERS}/$userUid").get()
            task.await().toObject(User::class.java)
        }
    }
}