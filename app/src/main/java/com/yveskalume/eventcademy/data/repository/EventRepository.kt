package com.yveskalume.eventcademy.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yveskalume.eventcademy.data.entity.Event
import com.yveskalume.eventcademy.data.entity.User
import com.yveskalume.eventcademy.data.util.FirestoreCollections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage
) {

    /**
     * Get all the upcoming events
     * @return a list of events
     */

    fun getAllUpComingEventsStream() = callbackFlow<List<Event>> {
        firestore.collection(FirestoreCollections.EVENTS)
            .whereGreaterThan(Event::endDate.name, Date())
            .whereEqualTo(Event::published.name, true)
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    close(error)
                    return@addSnapshotListener
                }
                value.toObjects(Event::class.java).also { data ->
                    trySend(data.sortedBy { it.startDate })
                }
            }
        awaitClose()
    }.flowOn(Dispatchers.IO)

    /**
     * Get an event by its uid
     * @param eventUid the uid of the event
     * @return the event if it exists and if the user is the owner or if the event is published
     * otherwise return null
     * @throws IllegalStateException if the user is not connected
     */

    suspend fun getEventByUid(eventUid: String): Event? {
        val userUid = firebaseAuth.uid
        return withContext(Dispatchers.IO) {
            val task = firestore.document("${FirestoreCollections.EVENTS}/$eventUid").get()
            val event = task.await().toObject(Event::class.java)
            if (event?.userUid == userUid) {
                return@withContext event
            }
            if (event?.published == true) {
                return@withContext event
            } else {
                return@withContext null
            }
        }
    }

    /**
     * Get all the events created by the current user
     * @return a list of events
     */
    private suspend fun uploadEventImage(event: Event): String {
        return withContext(Dispatchers.IO) {
            val imageUri = Uri.parse(event.imageUrl)
            val imageRef = firebaseStorage.reference.child("events/${event.uid}")
            val uploadTask = imageRef.putFile(imageUri)
            uploadTask.await()
            imageRef.downloadUrl.await().toString()
        }
    }

    /**
     * Get all the events created by the current user
     * @return a flow list of events in ascending order of creation date
     * @throws IllegalStateException if the user is not connected
     */

    fun getEventCreatedByCurrentUser() = callbackFlow<List<Event>> {
        val userUid = firebaseAuth.uid
        firestore.collection(FirestoreCollections.EVENTS)
            .whereEqualTo(Event::userUid.name, userUid)
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    close(error)
                    return@addSnapshotListener
                }
                value.toObjects(Event::class.java).also { data ->
                    trySend(data.sortedBy { it.createdAt })
                }
            }
        awaitClose()
    }.flowOn(Dispatchers.IO)

    /**
     * Create an event
     * @param event the event to create
     * @throws IllegalStateException if the user is not connected
     */
    suspend fun createEvent(event: Event) {
        withContext(Dispatchers.IO) {
            val user = firebaseAuth.currentUser
            if (user == null) {
                throw IllegalStateException("Vous devez être connecté pour créer un évènement")
            } else {
                val imageUrl = uploadEventImage(event)
                val eventToCreate = event.copy(
                    userUid = user.uid,
                    createdAt = Date(),
                    updatedAt = Date(),
                    imageUrl = imageUrl
                )
                val task = firestore.document("${FirestoreCollections.EVENTS}/${eventToCreate.uid}")
                    .set(eventToCreate)
                task.await()
            }
        }
    }

    /**
     * Delete an event
     * @param eventUid the uid of the event to delete
     */

    suspend fun deleteEvent(eventUid: String) {
        withContext(Dispatchers.IO) {
            firestore.document("${FirestoreCollections.EVENTS}/$eventUid").delete().await()
            firebaseStorage.reference.child("events/$eventUid").delete().await()
        }
    }

    suspend fun getUserByUid(userUid: String) : User? {
        return withContext(Dispatchers.IO) {
            val task = firestore.document("${FirestoreCollections.USERS}/$userUid").get()
            task.await().toObject(User::class.java)
        }
    }
}