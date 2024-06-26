package com.software3.paws_hub_android.model.dal.entity

import com.software3.paws_hub_android.model.dal.FirebaseResult


interface IFirebasePOST<Thing> {
    suspend fun save(obj: Thing): FirebaseResult<Boolean>
}