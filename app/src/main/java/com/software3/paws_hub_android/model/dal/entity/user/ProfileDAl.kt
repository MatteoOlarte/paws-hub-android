package com.software3.paws_hub_android.model.dal.entity.user

import com.google.firebase.firestore.FirebaseFirestore
import com.software3.paws_hub_android.core.ex.slugify
import com.software3.paws_hub_android.core.ex.toProfile
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.model.dal.FirebaseResult
import com.software3.paws_hub_android.model.dal.entity.IFirebaseDELETE
import com.software3.paws_hub_android.model.dal.entity.IFirebaseGET
import com.software3.paws_hub_android.model.dal.entity.IFirebasePOST
import kotlinx.coroutines.tasks.await


class ProfileDAl : IFirebaseGET<Profile>, IFirebasePOST<Profile>, IFirebaseDELETE<Profile> {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun delete(obj: Profile): FirebaseResult<Boolean> {
        return try {
            db.collection("users").document(obj.profileID).delete().await()
            FirebaseResult(true, null)
        } catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(false, ex)
        }
    }

    override suspend fun get(id: String): FirebaseResult<Profile?> {
        return try {
            val result = db.collection("users").document(id).get().await()
            FirebaseResult(result.toProfile(), null)
        } catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(null, ex)
        }
    }

    override suspend fun getAll(): FirebaseResult<List<Profile>> {
        TODO("Not yet implemented")
    }

    override suspend fun save(obj: Profile): FirebaseResult<Boolean> {
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
        return try {
            db.collection("users").document(obj.profileID).set(map).await()
            FirebaseResult(true, null)
        } catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(false, ex)
        }
    }

    suspend fun isUserNameAvailable(userID: String, username: String): Boolean {
        val ref = db.collection("users")
        val query = ref.whereNotEqualTo("_id", userID).whereEqualTo("user_name", username)
        val docs = query.get().await()
        return runCatching { docs.isEmpty }.getOrDefault(false)
    }

    suspend fun filterByUsernameContains(value: String): FirebaseResult<List<Profile>> {
        val ref = db.collection("users")
        val query = ref.orderBy("user_name").startAt(value).endAt("${value}\uf8ff")
        val docs = query.get().await().documents
        return try {
            val profiles = mutableListOf<Profile>()
            docs.forEach { profiles.add(it.toProfile()) }
            FirebaseResult(profiles, null)
        } catch (ex: Exception) {
            FirebaseResult(emptyList(), ex)
        }
    }
}