package com.yveskalume.eventcademy.core.data.firebase.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.yveskalume.eventcademy.core.domain.model.Advertisement
import com.yveskalume.eventcademy.core.domain.repository.AdvertisementRepository
import com.yveskalume.eventcademy.core.data.firebase.util.FirestoreCollections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AdvertisementRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AdvertisementRepository {

    override fun getAllAdvertisementsStream() = callbackFlow<List<Advertisement>> {
        val listener = firestore.collection(FirestoreCollections.ADVERTISEMENT)
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
        awaitClose { listener.remove() }
    }.flowOn(Dispatchers.IO)
}