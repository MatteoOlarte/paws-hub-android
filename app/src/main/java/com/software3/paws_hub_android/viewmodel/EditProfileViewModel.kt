package com.software3.paws_hub_android.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.dal.entity.UserDataObject
import com.software3.paws_hub_android.model.dal.storage.CloudStorage


class EditProfileViewModel : ViewModel() {
    private val currentUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private val userID: String = currentUser.uid
    val state = MutableLiveData<TransactionState>()
    val photoURI = MutableLiveData<Uri?>()


    fun setProfilePhotoURI(uri: Uri?) = uri?.let {
        photoURI.value = uri
    }

    fun updateProfile(
        fName: String,
        lName: String,
        uName: String,
        email: String,
        city: String? = null,
        phone: String? = null,
    ) {
        val userdata = UserData(userID, fName, lName, uName, city, email=email, phoneNumber=phone)
        val uri: Uri? = photoURI.value
        val storage: CloudStorage

        if (uri == null) {
            saveProfile(userdata)
            return
        }
        state.value = TransactionState.PENDING
        storage = CloudStorage(uri, userID)
        storage.child("users").child("profile-photos")
        storage.save().addOnFailureListener {
            userdata.photo = photoURI.value
            saveProfile(userdata)
            return@addOnFailureListener
        }.addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { downloadURL ->
                userdata.photo = downloadURL
                saveProfile(userdata)
            }.addOnFailureListener {
                state.postValue(TransactionState.FAILED)
            }
        }
    }

    private fun saveProfile(userData: UserData) {
        val dal = UserDataObject()
        dal.save(userData).addOnFailureListener {
            state.postValue(TransactionState.FAILED)
        }.addOnSuccessListener {
            state.postValue(TransactionState.SUCCESS)
        }
    }
}