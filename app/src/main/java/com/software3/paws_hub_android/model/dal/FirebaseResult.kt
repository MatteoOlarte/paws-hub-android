package com.software3.paws_hub_android.model.dal

data class FirebaseResult<Thing> (
    val result: Thing,
    val error: Throwable?
)