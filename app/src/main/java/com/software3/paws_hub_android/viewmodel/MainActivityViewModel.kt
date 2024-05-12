package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.model.dal.entity.user.ProfileDAl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivityViewModel : ViewModel() {
    private val _showProgressIndicator = MutableLiveData<Boolean>()
    val showProgressIndicator: LiveData<Boolean> = _showProgressIndicator

    private val _simpleSnackbarMessage = MutableLiveData<String>()
    val simpleSnackbarMessage: LiveData<String> = _simpleSnackbarMessage

    private val _profileData = MutableLiveData<Profile>()
    val profileData: LiveData<Profile> = _profileData

    private val _transactionState = MutableLiveData<TransactionState>()
    val transactionState: LiveData<TransactionState> = _transactionState


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

        if (user == null) {
            _transactionState.postValue(TransactionState.FAILED)
            return
        }
        _transactionState.postValue(TransactionState.PENDING)
        viewModelScope.launch(Dispatchers.IO) {
            val result = ProfileDAl().get(user.uid)
            if (result.result != null) {
                _transactionState.postValue(TransactionState.SUCCESS)
                _profileData.postValue(result.result)
            } else {
                _transactionState.postValue(TransactionState.FAILED)
            }
        }
    }
}