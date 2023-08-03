package com.yvkalume.eventcademy.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yvkalume.eventcademy.data.entity.Event
import com.yvkalume.eventcademy.data.util.FirestoreCollections
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class EventRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    fun getAllUpComingEventsStream() = callbackFlow<List<Event>> {
        firestore.collection(FirestoreCollections.EVENTS)
            .whereLessThan(Event::endDate.name, System.currentTimeMillis())
            .whereEqualTo(Event::isPublished.name, true)
            .orderBy(Event::endDate.name)
            .orderBy(Event::startDate.name, Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    close(error)
                    return@addSnapshotListener
                }
                value.toObjects(Event::class.java).also { data ->
                    trySend(data)
                }
            }
        awaitClose()
    }
}