package com.software3.paws_hub_android.model.dal.entity.api

import com.software3.paws_hub_android.model.dal.FirebaseResult

interface IFirebaseDELETE<Thing> {
    suspend fun delete(obj: Thing): FirebaseResult<Boolean>
}