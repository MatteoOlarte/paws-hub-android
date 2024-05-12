package com.software3.paws_hub_android.model.dal.storage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await

class CloudStorage (private val uri: Uri, private val name: String) {
    private val instance: FirebaseStorage = FirebaseStorage.getInstance()
    private var storageRef: StorageReference

    init {
        this.storageRef = instance.reference
    }

    fun child(pathString: String): CloudStorage {
        storageRef = storageRef.child(pathString)
        return this
    }

    fun save(): UploadTask {
        return storageRef.child(name).putFile(uri)
    }

    suspend fun saveAsync(): UploadTask.TaskSnapshot {
        return storageRef.child(name).putFile(uri).await()
    }
}