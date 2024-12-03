package com.yveskalume.eventcademy.core.data.firebase.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.yveskalume.eventcademy.core.data.firebase.util.FirebaseStorageFolders
import com.yveskalume.eventcademy.core.data.firebase.util.FirestoreCollections
import com.yveskalume.eventcademy.core.domain.model.Event
import com.yveskalume.eventcademy.core.domain.model.Post
import com.yveskalume.eventcademy.core.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage
): PostRepository {
    override suspend fun createPost(post: Post) {
        withContext(Dispatchers.IO){
            val user = firebaseAuth.currentUser
            if (user == null){
                throw IllegalStateException("Vous devez être connecté pour créer un Post")
            } else {
                val imageUrl = uploadPostImage(post)
                val postToCreate = post.copy(
                    userUid = user.uid,
                    createdAt = Date(),
                    updatedAt = Date(),
                    imageUrl = imageUrl
                )

                val task = firestore.document("${FirestoreCollections.POSTS}/${postToCreate.uid}")
                    .set(postToCreate)
                task.await()
            }
        }
    }

    private suspend fun uploadPostImage(post: Post): String{
        return withContext(Dispatchers.IO){
            val imageUri = Uri.parse(post.imageUrl)
            val imageRef = firebaseStorage
                .reference.child("${FirebaseStorageFolders.posts}/${post.uid}")
            val uploadTask = imageRef.putFile(imageUri)
            uploadTask.await()
            imageRef.downloadUrl.await().toString()
        }
    }

    override suspend fun deletePost(postUid: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getPostByUid(postUid: String): Post? {
        TODO("Not yet implemented")
    }

    override fun getAllPosts()= callbackFlow<List<Post>> {
        val listener = firestore.collection(FirestoreCollections.POSTS)
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    close(error)
                    return@addSnapshotListener
                }
                value.toObjects(Post::class.java).also { data ->
                    trySend(data.sortedBy { it.createdAt })
                }
            }
        awaitClose { listener.remove() }
    }.flowOn(Dispatchers.IO)
}