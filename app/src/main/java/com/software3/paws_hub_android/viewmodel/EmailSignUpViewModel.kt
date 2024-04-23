package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.core.ex.containsBlackOrNulls
import com.software3.paws_hub_android.core.ex.isEmailAddress
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.dal.FirebaseEmailAuth
import com.software3.paws_hub_android.model.dal.UserDataObject


class EmailSignUpViewModel : ViewModel() {
    val authState = MutableLiveData<TransactionState>()
    var message: String = ""
    var fName: String? = null
    var lName: String? = null
    var uName: String? = null
    var email: String? = null
    var password1: String? = null
    var password2: String? = null

    fun registerUser() {
        val fields = listOf(fName, lName, uName, email, password1, password2)
        if (fields.containsBlackOrNulls()) {
            message = "error_fields_required"
            authState.postValue(TransactionState.FAILED)
            return
        }
        if (email.isEmailAddress()) {
            message = "error_invalid_email"
            authState.postValue(TransactionState.FAILED)
            return
        }
        if (!validatePasswords(password1!!, password2!!)) {
            message = "error_password_mismatch"
            authState.postValue(TransactionState.FAILED)
            return
        }

        val auth = FirebaseEmailAuth(email!!, password1!!)
        authState.postValue(TransactionState.PENDING)

        auth.createUser().addOnFailureListener {
            message = it.message ?: ""
            authState.postValue(TransactionState.FAILED)
        }.addOnSuccessListener {
            val frUser = it.user

            if (frUser != null) {
                val user: UserData = userOf(frUser)
                val dal = UserDataObject()

                dal.save(user).addOnSuccessListener {
                    authState.postValue(TransactionState.SUCCESS)
                }.addOnFailureListener {
                    authState.postValue(TransactionState.FAILED)
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

    private fun validatePasswords(p1: String, p2: String) = p1 == p2
}