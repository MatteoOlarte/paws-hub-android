package com.software3.paws_hub_android.model.dal.entity.post

import android.util.Log
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.software3.paws_hub_android.core.ex.toPost
import com.software3.paws_hub_android.model.Post
import com.software3.paws_hub_android.model.dal.FirebaseResult
import com.software3.paws_hub_android.model.dal.entity.IFirebaseGET
import com.software3.paws_hub_android.model.dal.entity.IFirebasePOST
import kotlinx.coroutines.tasks.await
import org.jetbrains.annotations.ApiStatus.Experimental


class PostDAl : IFirebaseGET<Post>, IFirebasePOST<Post> {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun get(id: String): FirebaseResult<Post?> {
        return try {
            val result = db.collection("posts").document(id).get().await()
            FirebaseResult(result.toPost(), null)
        }
        catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(null, ex)
        }
    }

    override suspend fun getAll(): FirebaseResult<List<Post>> {
        return try {
            val result = db.collection("posts")
                .orderBy("pub_date", Query.Direction.DESCENDING)
                .get()
                .await()
            val docs = result.documents
            val elements = mutableListOf<Post>()

            if (docs.isEmpty()) {
                FirebaseResult(emptyList(), null)
            }
            else {
                docs.forEach { elements.add(it.toPost()) }
                FirebaseResult(elements, null)
            }
        }
        catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(emptyList(), ex)
        }
    }

    override suspend fun save(obj: Post): FirebaseResult<Boolean> {
        val author = mapOf(
            "_id" to obj.author["_id"],
            "full_name" to obj.author["full_name"],
            "username" to obj.author["username"],
            "profile_photo" to obj.author["profile_photo"]
        )
        val pet = mapOf(
            "_id" to obj.pet["_id"],
            "type" to obj.pet["type"],
            "breed" to obj.pet["breed"],
            "name" to obj.pet["name"],
        )
        val post = mapOf(
            "_id" to obj.postID,
            "image_url" to obj.image,
            "post_body" to obj.body,
            "pub_date" to obj.created,
            "type" to obj.type,
            "last_location" to obj.location,
            "author" to author,
            "pet" to pet
        )
        return try {
            db.collection("posts").document(obj.postID).set(post).await()
            FirebaseResult(true, null)
        }
        catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(false, ex)
        }
    }

    suspend fun filterByPetType(type: String): FirebaseResult<List<Post>> {
        return try {
            val result = db.collection("posts")
                .where(
                    Filter.and(
                        Filter.equalTo("type", "TYPE_DISCOVER"),
                        Filter.equalTo("pet.type", type)
                    )
                )
                .orderBy("pub_date", Query.Direction.DESCENDING)
                .get()
                .await()
            val docs = result.documents
            val elements = mutableListOf<Post>()

            if (docs.isEmpty()) {
                FirebaseResult(emptyList(), null)
            }
            else {
                docs.forEach { elements.add(it.toPost()) }
                FirebaseResult(elements, null)
            }
        }
        catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            Log.d("POST_DALL", ex.message.toString())
            FirebaseResult(emptyList(), ex)
        }
    }

    suspend fun filterByPostType(type: String): FirebaseResult<List<Post>> {
        return try {
            val query = db.collection("posts")
                .where(Filter.equalTo("type", type))
                .orderBy("pub_date")
                .get()
                .await()
            val docs = query.documents
            val elements = mutableListOf<Post>()

            if (docs.isEmpty()) {
                FirebaseResult(emptyList(), null)
            }
            else {
                docs.forEach { elements.add(it.toPost()) }
                FirebaseResult(elements, null)
            }
        }
        catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            Log.d("POST_DALL", ex.message.toString())
            FirebaseResult(emptyList(), ex)
        }
    }

    @Experimental
    suspend fun filterByPostLocation(location: String): FirebaseResult<List<Post>> {
        return try {
            val query = db.collection("posts")
                .where(Filter.and(
                    Filter.equalTo("type", "TYPE_FINDER"),
                    Filter.equalTo("last_location", location)
                ))
                .orderBy("pub_date")
                .get()
                .await()
            val docs = query.documents
            val elements = mutableListOf<Post>()

            if (docs.isEmpty()) {
                FirebaseResult(emptyList(), null)
            }
            else {
                docs.forEach { elements.add(it.toPost()) }
                FirebaseResult(elements, null)
            }
        }
        catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            Log.d("POST_DALL", ex.message.toString())
            FirebaseResult(emptyList(), ex)
        }
    }
}