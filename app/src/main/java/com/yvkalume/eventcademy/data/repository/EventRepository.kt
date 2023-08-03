package com.yvkalume.eventcademy.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yvkalume.eventcademy.data.entity.Event
import com.yvkalume.eventcademy.data.entity.fakeEvents
import com.yvkalume.eventcademy.data.util.FirestoreCollections
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
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
    }

//    suspend fun generateFakeEvents() {
//        val events = fakeEvents
//        events.forEach {
//            firestore.document("${FirestoreCollections.EVENTS}/${it.uid}").set(it).await()
//        }
//    }
}