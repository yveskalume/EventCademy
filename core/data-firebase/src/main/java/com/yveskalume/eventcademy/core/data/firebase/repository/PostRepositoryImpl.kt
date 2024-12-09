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

    /**
     * Create a post
     * @param post the post to create
     * @throws IllegalStateException if the user is not connected
     */
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
                    imageUrls = imageUrl
                )

                val task = firestore.document("${FirestoreCollections.POSTS}/${postToCreate.uid}")
                    .set(postToCreate)
                task.await()
            }
        }
    }


    /**
     * Get all the post created by the current user
     * @return a list of posts
     */

    private suspend fun uploadPostImage(post: Post): List<String>{
        return withContext(Dispatchers.IO){
            val urls = mutableListOf<String>()
            for ((index, imageUri) in post.imageUrls.withIndex()){
                val imageUrl = Uri.parse(imageUri)
                val imageRef = firebaseStorage
                    .reference.child("${FirebaseStorageFolders.posts}/${post.uid}/image_$index")

                val uploadTask = imageRef.putFile(imageUrl)
                uploadTask.await()
                urls.add(imageRef.downloadUrl.await().toString())
            }

            return@withContext urls
        }
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


    /**
     * Delete a post
     * @param postUid the uid of the post to delete
     */
    override suspend fun deletePost(postUid: String) {
        withContext(Dispatchers.IO){
            firestore.document("${FirestoreCollections.POSTS}/$postUid").delete().await()
            firebaseStorage.reference.child("${FirebaseStorageFolders.posts}/$postUid")
                .delete()
                .await()
        }
    }

    override suspend fun getPostByUid(postUid: String): Post? {
        return withContext(Dispatchers.IO){
            val task = firestore.document("${FirestoreCollections.POSTS}/$postUid").get()
            val post = task.await().toObject(Post::class.java)
            return@withContext post
        }
    }
}