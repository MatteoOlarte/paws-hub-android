package com.software3.paws_hub_android.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.software3.paws_hub_android.AuthState
import com.software3.paws_hub_android.model.FirebaseEmailAuth

class EmailAuthViewModel : IAuthViewModel, ViewModel() {
    val authState = MutableLiveData<AuthState>()
    var fName: String? = null
    var lName: String? = null
    var uName: String? = null
    var email: String? = null
    var password1: String? = null
    var password2: String? = null

    override fun register() {
        val fields = listOf(fName, lName, uName, email, password1, password2)
        if (isAnyBlank(fields) && !passwordsMatch() && !isEmailValid()) {
            authState.postValue(AuthState.FAILED)
            return
        }
        authState.postValue(AuthState.PENDING)
        val auth = FirebaseEmailAuth(email!!, password1!!)
        auth.createUser().addOnSuccessListener {
            authState.postValue(AuthState.SUCCESS)
        }.addOnFailureListener {
            authState.postValue(AuthState.FAILED)
        }
    }

    override fun login() {
        if (isAnyBlank(listOf(email, password1))) {
            authState.postValue(AuthState.FAILED)
            return
        }
        authState.postValue(AuthState.PENDING)

        val auth = FirebaseEmailAuth(email!!, password1!!)
        auth.signInUser().addOnSuccessListener {
            authState.postValue(AuthState.SUCCESS)
        }.addOnFailureListener {
            authState.postValue(AuthState.FAILED)
        }
    }

    private fun isAnyBlank(fields: List<String?>): Boolean {
        for (f in fields) {
            if (f.isNullOrBlank()) return true
        }
        return false
    }

    private fun isEmailValid(): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email!!)
    }

    private fun passwordsMatch(): Boolean {
        return password1 == password2
    }
}