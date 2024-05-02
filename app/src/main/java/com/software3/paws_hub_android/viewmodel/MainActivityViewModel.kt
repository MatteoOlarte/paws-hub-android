package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.dal.entity.UserDataObject


class MainActivityViewModel: ViewModel() {
    private val _showProgressIndicator = MutableLiveData<Boolean>()
    val showProgressIndicator: LiveData<Boolean> = _showProgressIndicator

    private val _simpleSnackbarMessage = MutableLiveData<String>()
    val simpleSnackbarMessage: LiveData<String> = _simpleSnackbarMessage

    private val _userdata = MutableLiveData<UserData>()
    val userdata: LiveData<UserData> = _userdata

    private val _userdataTransaction = MutableLiveData<TransactionState>()
    val userdataTransaction: LiveData<TransactionState> = _userdataTransaction

    fun startProgressIndicator() {
        _showProgressIndicator.postValue(true)
    }

    fun stopProgressIndicator() {
        _showProgressIndicator.postValue(false)
    }

    fun createSimpleSnackbarMessage(message: String) {
        _simpleSnackbarMessage.postValue(message)
    }

    fun fetchUserdata() {
        val user: FirebaseUser? = Firebase.auth.currentUser
        val dal = UserDataObject()

        if (user == null) {
            _userdataTransaction.postValue(TransactionState.FAILED)
            return
        }
        dal.get(user.uid).addOnSuccessListener {
            dal.cast(it).also { data ->
                _userdata.postValue(data)
                _userdataTransaction.postValue(TransactionState.SUCCESS)
            }
        }.addOnFailureListener {
            _userdataTransaction.postValue(TransactionState.FAILED)
        }
    }
}