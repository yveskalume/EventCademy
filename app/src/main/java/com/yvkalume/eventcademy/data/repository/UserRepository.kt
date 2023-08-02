package com.yvkalume.eventcademy.data.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.yvkalume.eventcademy.data.util.FirestoreCollections
import com.yvkalume.eventcademy.data.util.toDomainUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun signInWithCredential(credential: AuthCredential) {
        try {
            val user = firebaseAuth.signInWithCredential(credential).await().user
            saveUserToFirestore(user)
        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun saveUserToFirestore(firebaseUser: FirebaseUser?) {
        val user = firebaseUser?.toDomainUser()
        if (user != null) {
            firestore.collection(FirestoreCollections.USERS).document(user.uid).set(user).await()
        }
    }
}