package com.software3.paws_hub_android.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.firebase.UserDataDAO


class UserViewModel : ViewModel() {
    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val userData = MutableLiveData<UserData>()
    val isGuestUser = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    fun checkUserLoggedInStatus() = isGuestUser.postValue(currentUser == null)

    fun fetchUserData() {
        if (currentUser == null) {
            return
        }

        val dal = UserDataDAO()
        isLoading.postValue(true)
        dal.get(currentUser.uid).addOnSuccessListener {
            dal.cast(it).also { value ->
                isLoading.postValue(false)
                userData.postValue(value)
            }
        }.addOnFailureListener {

        }
    }

    fun logoutCurrentUser() {
        FirebaseAuth.getInstance().signOut()
        isGuestUser.postValue(true)
    }
}