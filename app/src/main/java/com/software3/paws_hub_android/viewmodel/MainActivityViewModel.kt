package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    private val _showProgressIndicator = MutableLiveData<Boolean>()
    val showProgressIndicator: LiveData<Boolean> = _showProgressIndicator

    private val _simpleSnackbarMessage = MutableLiveData<String>()
    val simpleSnackbarMessage: LiveData<String> = _simpleSnackbarMessage

    fun startProgressIndicator() {
        _showProgressIndicator.postValue(true)
    }

    fun stopProgressIndicator() {
        _showProgressIndicator.postValue(false)
    }

    fun createSimpleSnackbarMessage(message: String) {
        _simpleSnackbarMessage.postValue(message)
    }
}