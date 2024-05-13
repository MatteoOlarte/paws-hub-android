package com.software3.paws_hub_android.model.dal.entity.pet

import com.google.firebase.firestore.FirebaseFirestore
import com.software3.paws_hub_android.core.ex.toPet
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.dal.FirebaseResult
import com.software3.paws_hub_android.model.dal.entity.api.IFirebaseDELETE
import com.software3.paws_hub_android.model.dal.entity.api.IFirebaseGET
import com.software3.paws_hub_android.model.dal.entity.api.IFirebasePOST
import kotlinx.coroutines.tasks.await


class PetDAl : IFirebaseGET<Pet>, IFirebasePOST<Pet>, IFirebaseDELETE<Pet> {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun delete(obj: Pet): FirebaseResult<Boolean> {
        return try {
            db.collection("pets").document(obj.petID).delete().await()
            FirebaseResult(true, null)
        } catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(false, ex)
        }
    }

    override suspend fun get(id: String): FirebaseResult<Pet?> {
        return try {
            val result = db.collection("pet_breeds").document(id).get().await()
            FirebaseResult(result.toPet(), null)
        } catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(null, ex)
        }
    }

    override suspend fun getAll(): FirebaseResult<List<Pet>> {
        TODO("Not yet implemented")
    }

    override suspend fun save(obj: Pet): FirebaseResult<Boolean> {
        val breed = mapOf(
            "_id" to obj.breed?.get("_id"),
            "name" to obj.breed?.get("name")
        )
        val owner = mapOf(
            "_id" to obj.ownerID
        )
        val map = mapOf(
            "_id" to obj.petID,
            "name" to obj.name,
            "birth_date" to obj.birthDate,
            "weight" to obj.weight,
            "type" to obj.typeID,
            "breed" to breed,
            "notes" to obj.notes,
            "owner_id" to obj.ownerID
        )
        return try {
            db.collection("pets").document(obj.petID).set(map).await()
            FirebaseResult(true, null)
        } catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(false, ex)
        }
    }

    fun filterByOwner(ids: List<String>, callback: (List<Pet>) -> Any) {
        if (ids.isEmpty()) {
            callback(emptyList())
            return
        }

        db.collection("pets").whereIn("_id", ids).get().addOnSuccessListener {
            if (it.isEmpty) callback(emptyList())
            val documents = it.documents
            val pets = mutableListOf<Pet>()

            documents.forEach { document -> pets.add(document.toPet()) }
            callback(pets)
        }.addOnFailureListener {
            callback(emptyList())
        }
    }
}