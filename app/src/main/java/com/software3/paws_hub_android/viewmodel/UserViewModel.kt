package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.firebase.UserDataDAO


class UserViewModel: ViewModel() {
    val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val userData = MutableLiveData<UserData>()
    val isGuestUser = MutableLiveData<Boolean>()
    fun checkUserLoggedInStatus() = isGuestUser.postValue(currentUser == null)

    fun fetchUserData() {
        if (currentUser == null) {
            return
        }

        val dal = UserDataDAO()
        dal.get(currentUser.uid).addOnSuccessListener {
            val data = dal.cast(it)
            userData.postValue(data)
        }.addOnFailureListener {

        }
    }

    fun logoutCurrentUser() {
        FirebaseAuth.getInstance().signOut()
        checkUserLoggedInStatus()
    }
}