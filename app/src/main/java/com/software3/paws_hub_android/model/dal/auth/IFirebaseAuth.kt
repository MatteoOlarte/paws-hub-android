package com.software3.paws_hub_android.model.dal.auth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.model.UserSignIn


internal interface IFirebaseAuth {
    fun createUser(userData: Profile, key: String): Task<AuthResult>
    fun signInUser(data: UserSignIn, key: String): Task<AuthResult>
    fun updatePhoneNumber(userData: Profile): Task<Void>
    fun updateEmailAddress(userData: Profile): Task<Void>
}