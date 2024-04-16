package com.software3.paws_hub_android.model.firebase

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.software3.paws_hub_android.model.User


internal class FirebaseUser(private val user: User) : IFirebaseObject {
    private val db = FirebaseFirestore.getInstance()
    override fun save(o: IFirebaseObject): Task<Void> {
        val map = hashMapOf(
            "_id" to user._id,
            "first_name" to user.fName,
            "last_name" to user.lName,
            "user_name" to user.uName,
            "city" to user.city,
            "profile_photo" to user.photo,
            "email" to user.email,
            "phone_number" to user.phoneNumber
        )
        return db.collection("users").document(user._id).set(map)
    }
}