package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.dal.UserDataObject

class EditProfileViewModel() : ViewModel() {
    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val transactionState = MutableLiveData<TransactionState>()

    fun updateUser(data: UserData) {
        val user = currentUser
        val dal = UserDataObject()

        if (user == null) {
            transactionState.postValue(TransactionState.FAILED)
            return;
        }
        transactionState.postValue(TransactionState.PENDING)

        dal.save(data).addOnFailureListener {
            transactionState.postValue(TransactionState.FAILED)
        }.addOnSuccessListener {
            updateFirebaseProfile(data, user)
        }
    }

    private fun updateFirebaseProfile(data: UserData, user: FirebaseUser) {

    }
}