package com.yveskalume.eventcademy.data.repository

import com.google.firebase.firestore.FirebaseFirestore
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

class EventRepository @Inject constructor(private val firestore: FirebaseFirestore) {

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
}