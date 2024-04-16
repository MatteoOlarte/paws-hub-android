package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.software3.paws_hub_android.core.AuthState
import com.software3.paws_hub_android.model.firebase.FirebaseEmailAuth


class EmailSignInViewModel: ViewModel() {
    val authState = MutableLiveData<AuthState>()
    var message: String = ""
    var email: String? = null
    var password: String? = null

    fun login() {
        val fields = listOf(email, password)
        if (!validateFields(fields)) {
            message = "mensaje para campos vacios"
            authState.postValue(AuthState.FAILED)
            return
        }

        val auth = FirebaseEmailAuth(email!!, password!!)
        authState.postValue(AuthState.PENDING)

        auth.signInUser().addOnFailureListener {
            message = it.message ?: ""
            authState.postValue(AuthState.FAILED)
        }.addOnSuccessListener {
            authState.postValue(AuthState.SUCCESS)
        }
    }

    private fun validateFields(fields: List<String?>): Boolean {
        for (f in fields) {
            if (f.isNullOrBlank()) return false
        }
        return true
    }
}