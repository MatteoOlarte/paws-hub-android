package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.dal.UserDataObject

class EditProfileViewModel() : ViewModel() {
    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val transactionState = MutableLiveData<TransactionState>()

    fun updateUserData(
        fName: String,
        lName: String,
        uName: String,
        email: String,
        city: String? = null,
        phone: String? = null,
        photo: String? = null
    ) {
        val user = currentUser
        val dal = UserDataObject()
        val userdata: UserData

        if (user == null) {
            transactionState.postValue(TransactionState.FAILED)
            return;
        }
        userdata = UserData(user.uid, fName, lName, uName, city, photo, email, phone)
        dal.save(userdata).addOnSuccessListener {
            transactionState.postValue(TransactionState.SUCCESS)
        }
    }


}