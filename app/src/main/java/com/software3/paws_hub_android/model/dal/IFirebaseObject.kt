package com.software3.paws_hub_android.model.dal

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot

interface IFirebaseObject<T> {
    fun save(obj: T): Task<Void>
    fun get(id: String): Task<DocumentSnapshot>
    fun delete(obj: T): Task<Void>
    fun cast(doc: DocumentSnapshot): T
}