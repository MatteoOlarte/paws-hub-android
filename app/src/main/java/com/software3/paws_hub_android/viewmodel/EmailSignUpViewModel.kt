package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.software3.paws_hub_android.core.AuthState
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.firebase.FirebaseEmailAuth
import com.software3.paws_hub_android.model.firebase.UserDataDAO


class EmailSignUpViewModel : ViewModel() {
    val authState = MutableLiveData<AuthState>()
    var message: String = ""
    var fName: String? = null
    var lName: String? = null
    var uName: String? = null
    var email: String? = null
    var password1: String? = null
    var password2: String? = null

    fun registerUser() {
        val fields = listOf(fName, lName, uName, email, password1, password2)
        if (!validateFields(fields)) {
            message = "error_fields_required"
            authState.postValue(AuthState.FAILED)
            return
        }
        if (!validateEmail(email!!)) {
            message = "error_invalid_email"
            authState.postValue(AuthState.FAILED)
            return
        }
        if (!validatePasswords(password1!!, password2!!)) {
            message = "error_password_mismatch"
            authState.postValue(AuthState.FAILED)
            return
        }

        val auth = FirebaseEmailAuth(email!!, password1!!)
        authState.postValue(AuthState.PENDING)

        auth.createUser().addOnFailureListener {
            message = it.message ?: ""
            authState.postValue(AuthState.FAILED)
        }.addOnSuccessListener {
            val frUser = it.user

            if (frUser != null) {
                val user: UserData = userOf(frUser)
                val dal = UserDataDAO()

                dal.save(user).addOnSuccessListener {
                    authState.postValue(AuthState.SUCCESS)
                }.addOnFailureListener {
                    authState.postValue(AuthState.FAILED)
                }
            }
        }
    }

    private fun userOf(fUser: FirebaseUser): UserData {
        return UserData(
            _id = fUser.uid,
            fName = fName!!,
            lName = lName!!,
            uName = uName!!,
            city = null,
            photo = null,
            email = fUser.email,
            phoneNumber = fUser.phoneNumber
        )
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