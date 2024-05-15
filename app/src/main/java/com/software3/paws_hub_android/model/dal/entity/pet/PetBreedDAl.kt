package com.software3.paws_hub_android.model.dal.entity.pet

import com.google.firebase.firestore.FirebaseFirestore
import com.software3.paws_hub_android.core.ex.toPetBreed
import com.software3.paws_hub_android.model.PetBreed
import com.software3.paws_hub_android.model.PetType
import com.software3.paws_hub_android.model.dal.FirebaseResult
import com.software3.paws_hub_android.model.dal.entity.IFirebaseGET
import kotlinx.coroutines.tasks.await


class PetBreedDAl : IFirebaseGET<PetBreed> {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun get(id: String): FirebaseResult<PetBreed?> {
        return try {
            val result = db.collection("pet_breeds").document(id).get().await()
            FirebaseResult(result.toPetBreed(), null)
        } catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(null, ex)
        }
    }

    override suspend fun getAll(): FirebaseResult<List<PetBreed>> {
        return try {
            val result = db.collection("pet_breeds").get().await()
            val docs = result.documents
            val elements = mutableListOf<PetBreed>()

            if (docs.isEmpty()) {
                FirebaseResult(emptyList(), null)
            } else {
                docs.forEach { elements.add(it.toPetBreed()) }
                FirebaseResult(elements, null)
            }
        } catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(emptyList(), ex)
        }
    }

    suspend fun filterByType(type: PetType): FirebaseResult<List<PetBreed>>  {
        return try {
            val result = db.collection("pet_breeds").whereEqualTo("type_id", type.typeID).get().await()
            val docs = result.documents
            val elements = mutableListOf<PetBreed>()

            if (docs.isEmpty()) {
                FirebaseResult(emptyList(), null)
            } else {
                docs.forEach { elements.add(it.toPetBreed()) }
                FirebaseResult(elements, null)
            }
        } catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(emptyList(), ex)
        }
    }
}