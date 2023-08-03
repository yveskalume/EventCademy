package com.yvkalume.eventcademy.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.yvkalume.eventcademy.data.entity.Advertisement
import com.yvkalume.eventcademy.data.entity.fakeAdvertisementList
import com.yvkalume.eventcademy.data.util.FirestoreCollections
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AdvertisementRepository @Inject constructor(private val firestore: FirebaseFirestore) {

    fun getAllAdvertisementsStream() = callbackFlow<List<Advertisement>> {
        firestore.collection(FirestoreCollections.ADVERTISEMENT)
            .whereEqualTo(Advertisement::published.name, true)
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    close(error)
                    return@addSnapshotListener
                }
                value.toObjects(Advertisement::class.java).also { data ->
                    trySend(data)
                }
            }
        awaitClose()
    }

//    suspend fun generateFakeAdvertisements() {
//        val advertisements = fakeAdvertisementList
//        advertisements.forEach {
//            firestore.document("${FirestoreCollections.ADVERTISEMENT}/${it.uid}").set(it).await()
//        }
//    }
}