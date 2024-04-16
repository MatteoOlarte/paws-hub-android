package com.software3.paws_hub_android.model.firebase

import com.google.android.gms.tasks.Task

interface IFirebaseObject {
    fun save(o: IFirebaseObject): Task<Void>
}