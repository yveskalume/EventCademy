package com.yveskalume.eventcademy.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yveskalume.eventcademy.data.entity.Event
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


    suspend fun getEventByUid(eventUid: String): Event {
        return withContext(Dispatchers.IO) {
            val task = firestore.document("${FirestoreCollections.EVENTS}/$eventUid").get()
            val event = task.await().toObject(Event::class.java)
            if (event?.published == true) {
                return@withContext event
            } else {
                throw NoSuchElementException("Cet évènement est introuvable")
            }
        }
    }

    private suspend fun uploadEventImage(event: Event): String {
        return withContext(Dispatchers.IO) {
            val imageUri = Uri.parse(event.imageUrl)
            val imageRef = firebaseStorage.reference.child("events/${event.uid}")
            val uploadTask = imageRef.putFile(imageUri)
            uploadTask.await()
            imageRef.downloadUrl.await().toString()
        }
    }

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
}