package com.software3.paws_hub_android.model.dal.auth

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.model.UserSignIn


class FirebaseEmailAuth : IFirebaseAuth {
    override fun createUser(userData: Profile, key: String): Task<AuthResult> {
        val instance = Firebase.auth
        val email: String? = userData.email

        if (email == null) {
            val result = TaskCompletionSource<AuthResult>()
            result.setException(FirebaseAuthException("email_error", "email address is null"))
            return result.task
        }
        return instance.createUserWithEmailAndPassword(email, key)
    }

    override fun signInUser(data: UserSignIn, key: String): Task<AuthResult> {
        val instance = Firebase.auth
        val email: String? = data.email

        if (email == null) {
            val result = TaskCompletionSource<AuthResult>()
            result.setException(FirebaseAuthException("email_error", "email address is null"))
            return result.task
        }
        return instance.signInWithEmailAndPassword(email, key)
    }

    override fun updatePhoneNumber(userData: Profile): Task<Void> {
        TODO("Not yet implemented")
    }

    override fun updateEmailAddress(userData: Profile): Task<Void> {
        TODO("Not yet implemented")
    }
}