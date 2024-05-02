package com.software3.paws_hub_android.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.dal.entity.CityObject
import com.software3.paws_hub_android.model.dal.entity.UserDataObject
import com.software3.paws_hub_android.model.dal.storage.CloudStorage


class ProfileEditorViewModel : ViewModel() {
    private val _updateState = MutableLiveData<TransactionState>()
    val updateState: LiveData<TransactionState> = _updateState

    private val _userPhotoURI = MutableLiveData<Uri?>()
    val userPhotoURI: LiveData<Uri?> = _userPhotoURI

    private val _isNameAvailable = MutableLiveData<Boolean>()
    val isNameAvailable: LiveData<Boolean> = _isNameAvailable

    private val _userdata = MutableLiveData<UserData?>()
    val userdata: LiveData<UserData?> = _userdata

    private val _errorMSG = MutableLiveData<String?>()
    val errorMSG: LiveData<String?> = _errorMSG

    private val _citiesList = MutableLiveData<List<String>>()
    val citiesList: LiveData<List<String>> = _citiesList

    fun checkUsername(username: String) {
        val userID = Firebase.auth.currentUser?.uid
        val userDAL = UserDataObject()

        userID?.let {
            userDAL.isUsernameAvailable(userID, username) { _isNameAvailable.postValue(it) }
        }
    }

    fun fetchCityData() {
        val cityObject = CityObject()
        _citiesList.postValue(cityObject.getAll())
    }

    fun addProfilePhotoURI(uri: Uri?) = uri?.let { _userPhotoURI.postValue(uri) }

    fun setUserdata(data: UserData) = _userdata.postValue(data)

    fun saveProfile(
        fName: String,
        lName: String,
        uName: String,
        email: String,
        city: String? = null,
        phone: String? = null,
        preferredPet: String? = null
    ) {
        val data = _userdata.value
        val photoURI = _userPhotoURI.value
        val userDAL = UserDataObject()

        if (data == null || (_isNameAvailable.value == false)) {
            Log.d("ViewModelUser", "username")
            _updateState.postValue(TransactionState.FAILED)
            _errorMSG.postValue("error_fill")
            return
        }

        _updateState.postValue(TransactionState.PENDING)
        trySavePhotoInStorage(photoURI, data._id) {
            data.fName = fName
            data.lName = lName
            data.uName = uName
            data.email = email
            data.city = city
            data.phoneNumber = phone
            data.preferredPet = preferredPet
            data.photo = it ?: data.photo

            userDAL.save(data).addOnSuccessListener {
                _updateState.postValue(TransactionState.SUCCESS)
            }.addOnFailureListener {
                _updateState.postValue(TransactionState.FAILED)
            }
        }
    }

    private fun trySavePhotoInStorage(uri: Uri?, userID: String, callback: (url: Uri?) -> Unit) {
        if (uri == null) {
            callback(null)
            return
        }
        val cloudStorage = CloudStorage(uri, userID)
        cloudStorage.child("users").child("profile-photos").save().addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { downloadURL -> callback(downloadURL)}
        }.addOnFailureListener {
            callback(null)
        }
    }
}