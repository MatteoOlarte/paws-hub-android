package com.software3.paws_hub_android.model.dal.entity.user

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.software3.paws_hub_android.core.ex.slugify
import com.software3.paws_hub_android.core.ex.toMutableListOrEmpty
import com.software3.paws_hub_android.core.ex.toURI
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.model.dal.entity.IFirebaseObject


class UserDataDAL : IFirebaseObject<Profile> {
    private val db = FirebaseFirestore.getInstance()

    override fun save(obj: Profile): Task<Void> {
        val map = hashMapOf(
            "_id" to obj.profileID,
            "first_name" to obj.fName,
            "last_name" to obj.lName,
            "user_name" to obj.uName.slugify(),
            "city" to obj.city,
            "profile_photo" to obj.photo,
            "email" to obj.email,
            "phone_number" to obj.phoneNumber,
            "preferred_pet" to obj.preferredPet,
            "pets" to obj.pets
        )
        return db.collection("users").document(obj.profileID).set(map)
    }

    override fun get(id: String): Task<DocumentSnapshot> {
        return db.collection("users").document(id).get()
    }

    override fun delete(obj: Profile): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun cast(doc: DocumentSnapshot): Profile {
        return Profile(
            profileID = doc.id,
            fName = doc.get("first_name") as String,
            lName = doc.get("last_name") as String,
            uName = doc.get("user_name") as String,
            email = doc.get("email") as String?,
            city = doc.get("city") as String?,
            photo = (doc.get("profile_photo") as String?)?.toURI(),
            phoneNumber = doc.get("phone_number") as String?,
            preferredPet = doc.get("preferred_pet") as String?,
            pets = doc.get("pets").toMutableListOrEmpty<String>()
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