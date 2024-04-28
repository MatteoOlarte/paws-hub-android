package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.software3.paws_hub_android.core.enums.AuthProvider
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.UserSignIn
import com.software3.paws_hub_android.model.dal.auth.FirebaseEmailAuth
import com.software3.paws_hub_android.model.dal.auth.IFirebaseAuth


class SignInViewModel : ViewModel() {
    val authState = MutableLiveData<TransactionState>()
    var errorstr: String = ""
        private set


    fun loginIn(data: UserSignIn, password: String, provider: AuthProvider) = when (provider) {
        AuthProvider.EMAIL_PASSWORD -> loginInWithProvider(FirebaseEmailAuth(), data, password)
        else -> authState.value = TransactionState.FAILED
    }

    private fun loginInWithProvider(auth: IFirebaseAuth, data: UserSignIn, password: String) {
        authState.postValue(TransactionState.PENDING)
        auth.signInUser(data, password).addOnFailureListener {
            errorstr = it.message.toString()
            authState.postValue(TransactionState.FAILED)
        }.addOnSuccessListener {
            authState.postValue(TransactionState.SUCCESS)
        }
    }
}