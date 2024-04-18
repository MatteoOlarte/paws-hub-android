package com.software3.paws_hub_android.model.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.software3.paws_hub_android.model.UserData


internal class UserDataDAO : IFirebaseObject<UserData> {
    private val db = FirebaseFirestore.getInstance()

    override fun save(obj: UserData): Task<Void> {
        val map = hashMapOf(
            "_id" to obj._id,
            "first_name" to obj.fName,
            "last_name" to obj.lName,
            "user_name" to obj.uName,
            "city" to obj.city,
            "profile_photo" to obj.photo,
            "email" to obj.email,
            "phone_number" to obj.phoneNumber
        )
        return db.collection("users").document(obj._id).set(map)
    }

    override fun get(id: String): Task<DocumentSnapshot> {
        return db.collection("users").document(id).get()
    }

    override fun cast(doc: DocumentSnapshot): UserData {
        return UserData(
            _id = doc.id,
            fName = doc.get("first_name") as String,
            lName = doc.get("last_name") as String,
            uName = doc.get("user_name") as String,
            email = doc.get("user_name") as String?,
            city = doc.get("city") as String?,
            photo = doc.get("profile_photo") as String?,
            phoneNumber = doc.get("phone_number") as String?
        )
    }
}