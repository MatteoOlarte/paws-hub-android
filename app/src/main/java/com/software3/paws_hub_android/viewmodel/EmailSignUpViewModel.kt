package com.software3.paws_hub_android.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.software3.paws_hub_android.AuthState
import com.software3.paws_hub_android.model.User
import com.software3.paws_hub_android.model.firebase.FirebaseEmailAuth


class EmailSignUpViewModel : ViewModel() {
    val authState = MutableLiveData<AuthState>()
    var message: String = ""
    var fName: String? = null
    var lName: String? = null
    var uName: String? = null
    var email: String? = null
    var password1: String? = null
    var password2: String? = null

    fun register() {
        val fields = listOf(fName, lName, uName, email, password1, password2)
        if (!validateFields(fields)) {
            message = "mensaje para campos vacios"
            authState.postValue(AuthState.FAILED)
            return
        }
        if (!validateEmail(email!!)) {
            message = "mensaje para email invalido"
            authState.postValue(AuthState.FAILED)
            return
        }
        if (!validatePasswords(password1!!, password2!!)) {
            message = "mensaje para pass invalido"
            authState.postValue(AuthState.FAILED)
            return
        }

        val auth = FirebaseEmailAuth(email!!, password1!!)
        authState.postValue(AuthState.PENDING)

        auth.createUser().addOnSuccessListener {
            Log.d("EmailSignUpViewModel", it.user?.email ?: "null")
            authState.postValue(AuthState.SUCCESS)
        }.addOnFailureListener {
            message = it.message ?: ""
            authState.postValue(AuthState.FAILED)
        }
    }

    private fun validateFields(fields: List<String?>): Boolean {
        for (f in fields) {
            if (f.isNullOrBlank()) return false
        }
        return true
    }

    private fun validateEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }

    private fun validatePasswords(p1: String, p2: String) = p1 == p2
}