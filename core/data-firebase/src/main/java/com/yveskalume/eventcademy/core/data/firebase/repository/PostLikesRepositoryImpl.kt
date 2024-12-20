package com.yveskalume.eventcademy.core.data.firebase.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yveskalume.eventcademy.core.data.firebase.util.FirestoreCollections
import com.yveskalume.eventcademy.core.domain.model.Post
import com.yveskalume.eventcademy.core.domain.model.PostLike
import com.yveskalume.eventcademy.core.domain.repository.PostLikesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class PostLikesRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth

):PostLikesRepository {

    override fun getAllLikesByPostUid(postUid: String) = callbackFlow<List<PostLike>> {
        val listener = firestore.collection(FirestoreCollections.LIKES)
            .whereEqualTo(PostLike::postUid.name, postUid)
            .addSnapshotListener{value, error->
                if (error != null || value == null){
                    close(error)
                    return@addSnapshotListener
                }
                value.toObjects(PostLike::class.java).also {
                    trySend(it)
                }
            }
        awaitClose{ listener.remove() }
    }.flowOn(Dispatchers.IO)

    override fun getAllUserPostLikes() = callbackFlow<List<PostLike>> {
        val currentUser = firebaseAuth.currentUser
        val listener = firestore.collection(FirestoreCollections.LIKES)
            .whereEqualTo(PostLike::userUid.name, currentUser?.uid)
            .addSnapshotListener{value, error->
                if (error != null || value == null){
                    close(error)
                    return@addSnapshotListener
                }
                value.toObjects(PostLike::class.java).also {data->
                    trySend(data.sortedBy { it.createdAt })
                }
            }
        awaitClose{listener.remove()}
    }.flowOn(Dispatchers.IO)

    override suspend fun checkIfUserHasLiked(postUid: String) = callbackFlow {
        val currentUser =  firebaseAuth.currentUser
        val listener = firestore
            .document("${FirestoreCollections.LIKES}/${currentUser?.uid}-$postUid")
            .addSnapshotListener{value, error ->
                if (error != null || value == null){
                    Log.e("checkIfUserHasLiked","error",error)
                    trySend(false)
                    return@addSnapshotListener
                }
                trySend(value.exists())
            }
        awaitClose{listener.remove()}
    }.flowOn(Dispatchers.IO)

    override suspend fun createLike(post: Post) {
        withContext(Dispatchers.IO){
            val currentUser = firebaseAuth.currentUser ?: return@withContext
            val postLike = PostLike(
                uid = "${currentUser.uid}-${post.uid}",
                postUid = post.uid,
                postImageUrl = post.imageUrls,
                userUid = currentUser.uid,
                userName = currentUser.displayName ?: "",
                email = currentUser.email ?: "",
                userPhotoUrl = currentUser.photoUrl.toString(),
                createdAt = Date().toString()
            )
            firestore.document("${FirestoreCollections.LIKES}/${postLike.uid}")
                .set(postLike)
                .await()
        }
    }

    override suspend fun deleteLike(postUid: String) {
        withContext(Dispatchers.IO){
            val current = firebaseAuth.currentUser ?: return@withContext
            firestore.document("${FirestoreCollections.LIKES}/${current.uid}-$postUid")
                .delete()
                .await()
        }
    }
}