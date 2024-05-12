package com.software3.paws_hub_android.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.model.dal.entity.user.CityObject
import com.software3.paws_hub_android.model.dal.entity.user.ProfileDAl
import com.software3.paws_hub_android.model.dal.storage.CloudStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileEditorViewModel : ViewModel() {
    private val _updateState = MutableLiveData<TransactionState>()
    val updateState: LiveData<TransactionState> = _updateState

    private val _userPhotoURI = MutableLiveData<Uri?>()
    val userPhotoURI: LiveData<Uri?> = _userPhotoURI

    private val _isNameAvailable = MutableLiveData<Boolean>()
    val isNameAvailable: LiveData<Boolean> = _isNameAvailable

    private val _userdata = MutableLiveData<Profile?>()
    val userdata: LiveData<Profile?> = _userdata

    private val _errorMSG = MutableLiveData<String?>()
    val errorMSG: LiveData<String?> = _errorMSG

    private val _citiesList = MutableLiveData<List<String>>()
    val citiesList: LiveData<List<String>> = _citiesList


    fun checkUsername(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Firebase.auth.currentUser?.uid?.let {
                val result = ProfileDAl().isUserNameAvailable(it, username)
                _isNameAvailable.postValue(result)
            }
        }
    }

    fun fetchCityData() {
        val cityObject = CityObject()
        _citiesList.postValue(cityObject.getAll())
    }

    fun setPhotoURL(uri: Uri?) = uri?.let { _userPhotoURI.postValue(uri) }

    fun setUserdata(data: Profile) = _userdata.postValue(data)

    fun saveProfile(
        fName: String,
        lName: String,
        uName: String,
        email: String,
        city: String? = null,
        phone: String? = null,
        preferredPet: String? = null
    ) {
        val userProfile = _userdata.value
        val photoURI = _userPhotoURI.value

        if (userProfile == null || (_isNameAvailable.value == false)) {
            _updateState.postValue(TransactionState.FAILED)
            _errorMSG.postValue("error_fill")
            return
        }
        _updateState.postValue(TransactionState.PENDING)
        trySavePhotoInStorage(photoURI, userProfile.profileID) {
            userProfile.fName = fName
            userProfile.lName = lName
            userProfile.uName = uName
            userProfile.email = email
            userProfile.city = city
            userProfile.phoneNumber = phone
            userProfile.preferredPet = preferredPet
            userProfile.photo = it ?: userProfile.photo
            onProfileSave(userProfile)
        }
    }

    private fun trySavePhotoInStorage(uri: Uri?, userID: String, callback: (url: Uri?) -> Unit) {
        if (uri == null) {
            callback(null)
            return
        }
        val storage = CloudStorage(uri, userID).child("users")
        storage.child("profile-photos").save().addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { downloadURL -> callback(downloadURL) }
        }.addOnFailureListener {
            callback(null)
        }
    }

    private fun onProfileSave(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = ProfileDAl().save(profile)

            if (result.result) {
                _updateState.postValue(TransactionState.SUCCESS)
            } else {
                _updateState.postValue(TransactionState.FAILED)
                _errorMSG.postValue(result.error?.message)
            }
        }
    }
}