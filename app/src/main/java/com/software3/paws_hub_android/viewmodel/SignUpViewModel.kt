package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import com.software3.paws_hub_android.core.enums.AuthProvider
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.dal.entity.UserDataObject
import com.software3.paws_hub_android.model.dal.auth.FirebaseEmailAuth
import com.software3.paws_hub_android.model.dal.auth.IFirebaseAuth


class SignUpViewModel : ViewModel() {
    val authState = MutableLiveData<TransactionState>()
    var errorstr: String = ""
        private set


    fun createUser(
        userData: UserData,
        password: String,
        passwordConfirm: String,
        provider: AuthProvider
    ) = when (provider) {
        AuthProvider.EMAIL_PASSWORD -> createUserWithProvider(
            FirebaseEmailAuth(),
            userData,
            password,
            passwordConfirm
        )
        else -> authState.value = TransactionState.FAILED
    }

    private fun createUserWithProvider(
        auth: IFirebaseAuth,
        data: UserData,
        password1: String,
        password2: String
    ) {
        if (password1 != password2) {
            errorstr = "error_password_mismatch"
            authState.postValue(TransactionState.FAILED)
            return
        }

        authState.postValue(TransactionState.PENDING)
        auth.createUser(data, password1).addOnFailureListener {
            errorstr = it.message.toString()
            authState.postValue(TransactionState.FAILED)
        }.addOnSuccessListener {
            val fUser: FirebaseUser? = it.user
            fUser?.let { user ->
                createUserData(user, data)
            }
        }
    }

    private fun createUserData(firebaseUser: FirebaseUser, data: UserData) {
        val userData = UserData(
            _id = firebaseUser.uid,
            fName = data.fName,
            lName = data.lName,
            uName = data.uName,
            city = data.city,
            photo = data.photo,
            email = data.email,
            phoneNumber = data.phoneNumber
        )
        val dal = UserDataObject()

        dal.save(userData).addOnFailureListener {
            errorstr = "user_creation_error"
            authState.postValue(TransactionState.FAILED)
        }.addOnSuccessListener {
            authState.postValue(TransactionState.SUCCESS)
        }
    }
}