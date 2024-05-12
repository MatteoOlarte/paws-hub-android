package com.software3.paws_hub_android.model.dal.entity.api

import com.software3.paws_hub_android.model.dal.FirebaseResult


interface IFirebaseGET<Thing> {
    suspend fun get(id: String): FirebaseResult<Thing?>
    suspend fun getAll(): FirebaseResult<List<Thing>>
}