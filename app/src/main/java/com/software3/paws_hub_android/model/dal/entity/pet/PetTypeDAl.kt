package com.software3.paws_hub_android.model.dal.entity.pet

import com.google.firebase.firestore.FirebaseFirestore
import com.software3.paws_hub_android.core.ex.toPetType
import com.software3.paws_hub_android.model.PetType
import com.software3.paws_hub_android.model.dal.FirebaseResult
import com.software3.paws_hub_android.model.dal.entity.IFirebaseGET
import kotlinx.coroutines.tasks.await


class PetTypeDAl : IFirebaseGET<PetType> {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun get(id: String): FirebaseResult<PetType?> {
        return try {
            val result = db.collection("pet_types").document(id).get().await()
            FirebaseResult(result.toPetType(), null)
        } catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(null, ex)
        }
    }

    override suspend fun getAll(): FirebaseResult<List<PetType>> {
        return try {
            val result = db.collection("pet_types").get().await()
            val docs = result.documents
            val elements = mutableListOf<PetType>()

            if (docs.isEmpty()) {
                FirebaseResult(emptyList(), null)
            } else {
                docs.forEach { elements.add(it.toPetType()) }
                FirebaseResult(elements, null)
            }
        } catch (ex: Exception) {
            //FirebaseCrashlytics.getInstance().recordException(ex)
            FirebaseResult(emptyList(), ex)
        }
    }
}