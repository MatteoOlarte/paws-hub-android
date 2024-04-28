package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.dal.entity.UserDataObject


class UserViewModel : ViewModel() {
    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val userdata = MutableLiveData<UserData>()
    val isGuestUser = MutableLiveData<Boolean>()
    val transactionState = MutableLiveData<TransactionState>()

    fun fetchUserData() {
        val user = currentUser
        val dal = UserDataObject()

        if (user == null) {
            transactionState.postValue(TransactionState.FAILED)
            return
        }
        dal.get(user.uid).addOnSuccessListener {
            dal.cast(it).also { data ->
                userdata.postValue(data)
                transactionState.postValue(TransactionState.SUCCESS)
            }
        }.addOnFailureListener {
            transactionState.postValue(TransactionState.FAILED)
        }
    }

    fun logoutCurrentUser() {
        FirebaseAuth.getInstance().signOut()
        isGuestUser.postValue(true)
    }

    fun checkUserLoggedInStatus() = isGuestUser.postValue(currentUser == null)
}