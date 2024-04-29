package com.software3.paws_hub_android.model.dal.entity

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.software3.paws_hub_android.core.ex.slugify
import com.software3.paws_hub_android.core.ex.toURI
import com.software3.paws_hub_android.model.UserData


class UserDataObject : IFirebaseObject<UserData> {
    private val db = FirebaseFirestore.getInstance()

    override fun save(obj: UserData): Task<Void> {
        val map = hashMapOf(
            "_id" to obj._id,
            "first_name" to obj.fName,
            "last_name" to obj.lName,
            "user_name" to obj.uName.slugify(),
            "city" to obj.city,
            "profile_photo" to obj.photo,
            "email" to obj.email,
            "phone_number" to obj.phoneNumber,
            "preferred_pet" to obj.preferredPet
        )
        return db.collection("users").document(obj._id).set(map)
    }

    override fun get(id: String): Task<DocumentSnapshot> {
        return db.collection("users").document(id).get()
    }

    override fun delete(obj: UserData): Task<Void> {
        throw NotImplementedError()
    }

    override fun cast(doc: DocumentSnapshot): UserData {
        return UserData(
            _id = doc.id,
            fName = doc.get("first_name") as String,
            lName = doc.get("last_name") as String,
            uName = doc.get("user_name") as String,
            email = doc.get("email") as String?,
            city = doc.get("city") as String?,
            photo = (doc.get("profile_photo") as String?)?.toURI(),
            phoneNumber = doc.get("phone_number") as String?,
            preferredPet = doc.get("preferred_pet") as String?
        )
    }

    fun isUsernameAvailable(id: String, username: String, callback: (Boolean) -> Unit) {
        val document: CollectionReference = db.collection("users")

        document.whereNotEqualTo("_id", id).whereEqualTo("user_name", username).get()
            .addOnSuccessListener {
                Log.d("user_data", it.toString())
                callback(it.isEmpty)
            }.addOnFailureListener {
                Log.d("user_data", it.toString())
                callback(false)
            }
    }
}