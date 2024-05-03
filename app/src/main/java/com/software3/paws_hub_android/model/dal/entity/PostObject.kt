package com.software3.paws_hub_android.model.dal.entity

import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.software3.paws_hub_android.core.ex.toURI
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.Post

class PostObject: IFirebaseObject<Post> {
    private val db = FirebaseFirestore.getInstance()

    override fun save(obj: Post): Task<Void> {
        val map = mapOf(
            "_id" to obj.postID,
            "image_url" to obj.image,
            "post_body" to obj.body,
            "pub_date" to obj.pupDate,
            "author_full_name" to obj.authorFullName,
            "author_username" to obj.authorUsername,
            "author_img_url" to obj.authorProfilePicture,
            "pet_name" to obj.petName,
            "author_id" to obj.authorID,
            "pet_id" to obj.petID
        )
        return db.collection("posts").document(obj.postID).set(map)
    }

    override fun get(id: String): Task<DocumentSnapshot> {
        TODO("Not yet implemented")
    }

    override fun cast(doc: DocumentSnapshot): Post = Post(
        postID = doc.get("_id") as String,
        image = (doc.get("image_url") as String).toURI(),
        body = doc.get("post_body") as String,
        pupDate = (doc.get("pub_date") as Timestamp).toDate(),
        authorFullName = doc.get("author_full_name") as String,
        authorUsername = doc.get("author_username") as String,
        authorProfilePicture = (doc.get("author_img_url") as? String)?.toURI(),
        petName = doc.get("pet_name") as String,
        authorID = doc.get("author_id") as String,
        petID = doc.get("pet_id") as String,
    )

    override fun delete(obj: Post): Task<Void> {
        TODO("Not yet implemented")
    }

    fun getAll(callback: (posts: List<Post>?) -> Unit) {
        db.collection("posts").get().addOnSuccessListener {
            if (it.isEmpty) callback(emptyList())
            val documents = it.documents
            val posts = mutableListOf<Post>()
            documents.forEach { document -> posts.add(cast(document)) }
            callback(posts)
        }.addOnFailureListener {
            callback(null)
        }
    }
}