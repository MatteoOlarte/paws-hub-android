package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.software3.paws_hub_android.core.AuthState
import com.software3.paws_hub_android.model.firebase.UserDataDAO

class EditProfileViewModel: ViewModel() {
    val authState = MutableLiveData<AuthState>()
    var message: String = ""
    var fName: String? = null
    var lName: String? = null
    var uName: String? = null
    var email: String? = null
    var city: String? = null
    var phone: String? = null
    var photo: String? = null

    fun beginEditTransaction() {

    }

    private fun getUser() {

    }

    private fun getImage() {

    }
}