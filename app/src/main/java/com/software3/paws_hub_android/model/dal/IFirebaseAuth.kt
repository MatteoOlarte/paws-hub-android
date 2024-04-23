package com.software3.paws_hub_android.model.dal

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

internal interface IFirebaseAuth {
    fun createUser(): Task<AuthResult>
    fun signInUser(): Task<AuthResult>
}