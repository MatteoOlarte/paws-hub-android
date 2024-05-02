package com.software3.paws_hub_android.model.dal.entity

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.software3.paws_hub_android.model.Pet
import java.util.Date

class PetObject: IFirebaseObject<Pet> {
    private val db = FirebaseFirestore.getInstance()

    override fun save(obj: Pet): Task<Void> {
        val map = mapOf(
            "_id" to obj.petID,
            "name" to obj.name,
            "birth_date" to obj.birthDate,
            "weight" to obj.weight,
            "type" to obj.type,
            "breed" to obj.breed,
            "notes" to obj.notes,
            "owner_id" to obj.ownerID
        )
        return db.collection("pets").document(obj.petID).set(map)
    }

    override fun get(id: String): Task<DocumentSnapshot> {
        return db.collection("pets").document(id).get()
    }

    override fun delete(obj: Pet): Task<Void> {
        return db.collection("pets").document(obj.petID).delete()
    }

    override fun cast(doc: DocumentSnapshot) = Pet(
        petID = doc.get("_id") as String,
        name = doc.get("name") as String,
        birthDate = doc.get("birth_date") as Date,
        weight = doc.get("weight") as Float,
        type = doc.get("type") as String,
        breed = doc.get("breed") as String,
        notes = doc.get("notes") as String?,
        ownerID = doc.get("owner_id") as String
    )
}