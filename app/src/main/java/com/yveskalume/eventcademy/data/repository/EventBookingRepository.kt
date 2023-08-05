package com.yveskalume.eventcademy.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yveskalume.eventcademy.data.entity.Event
import com.yveskalume.eventcademy.data.entity.EventBooking
import com.yveskalume.eventcademy.data.util.FirestoreCollections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class EventBookingRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    fun getAllBookingByEventUid(eventUid: String) = callbackFlow<List<EventBooking>> {
        val listener = firestore.collection(FirestoreCollections.BOOKINGS)
            .whereEqualTo(EventBooking::eventUid.name, eventUid)
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    close(error)
                    return@addSnapshotListener
                }
                value.toObjects(EventBooking::class.java).also { data ->
                    trySend(data)
                }
            }
        awaitClose { listener.remove() }
    }.flowOn(Dispatchers.IO)


    fun getAllUserEventBookings() = callbackFlow<List<EventBooking>> {
        val currentUser = firebaseAuth.currentUser
        val listener = firestore.collection(FirestoreCollections.BOOKINGS)
            .whereEqualTo(EventBooking::userUid.name, currentUser?.uid)
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    close(error)
                    return@addSnapshotListener
                }
                value.toObjects(EventBooking::class.java).also { data ->
                    trySend(data.sortedBy { it.eventDate })
                }
            }
        awaitClose { listener.remove() }
    }.flowOn(Dispatchers.IO)

    suspend fun checkIfUserHasBooked(eventUid: String) = callbackFlow {
        val currentUser = firebaseAuth.currentUser
        val listener = firestore
            .document("${FirestoreCollections.BOOKINGS}/${currentUser?.uid}-$eventUid")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    trySend(false)
                    return@addSnapshotListener
                }
                trySend(value.exists())
            }
        awaitClose { listener.remove() }
    }.flowOn(Dispatchers.IO)

    suspend fun createBooking(event: Event) {
        withContext(Dispatchers.IO) {
            val currentUser = firebaseAuth.currentUser ?: return@withContext
            val eventBooking = EventBooking(
                eventUid = event.uid,
                eventName = event.name,
                eventDate = event.startDate,
                eventImageUrl = event.imageUrl,
                eventLocation = event.location,
                userUid = currentUser.uid,
                userName = currentUser.displayName ?: "",
                email = currentUser.email ?: "",
                userPhotoUrl = currentUser.photoUrl?.toString() ?: "",
                createdAt = Date().toString()
            )
            firestore.document("${FirestoreCollections.BOOKINGS}/${currentUser.uid}-${event.uid}")
                .set(eventBooking)
                .await()
        }
    }

    suspend fun deleteBooking(eventUid: String) {
        withContext(Dispatchers.IO) {
            val currentUser = firebaseAuth.currentUser ?: return@withContext
            firestore.document("${FirestoreCollections.BOOKINGS}/${currentUser.uid}-$eventUid")
                .delete()
                .await()
        }
    }
}