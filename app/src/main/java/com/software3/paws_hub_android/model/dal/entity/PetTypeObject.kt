package com.software3.paws_hub_android.model.dal.entity

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.software3.paws_hub_android.core.ex.toListOrEmpty
import com.software3.paws_hub_android.model.PetType


class PetTypeObject: IFirebaseObject<PetType> {
    private val db = FirebaseFirestore.getInstance()

    override fun save(obj: PetType): Task<Void> {
        throw NotImplementedError()
    }

    override fun delete(obj: PetType): Task<Void> {
        throw NotImplementedError()
    }

    override fun get(id: String): Task<DocumentSnapshot> {
        return db.collection("breeds").document(id).get()
    }

    override fun cast(doc: DocumentSnapshot) = PetType(
        typeID = (doc.get("_id") as String),
        breeds = (doc.get("breeds") as Any).toListOrEmpty()
    )

    internal fun getSupportedPetTypes(): List<String> {
        return listOf("type_dog", "type_cat")
    }
}