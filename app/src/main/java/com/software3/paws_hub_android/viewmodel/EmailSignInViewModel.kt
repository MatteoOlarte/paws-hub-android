package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.core.ex.containsBlackOrNulls
import com.software3.paws_hub_android.model.dal.FirebaseEmailAuth


class EmailSignInViewModel: ViewModel() {
    val authState = MutableLiveData<TransactionState>()
    var message: String = ""
    var email: String? = null
    var password: String? = null

    fun login() {
        val fields = listOf(email, password)
        if (fields.containsBlackOrNulls()) {
            message = "error_fields_required"
            authState.postValue(TransactionState.FAILED)
            return
        }

        val auth = FirebaseEmailAuth(email!!, password!!)
        authState.postValue(TransactionState.PENDING)

        auth.signInUser().addOnFailureListener {
            message = it.message ?: ""
            authState.postValue(TransactionState.FAILED)
        }.addOnSuccessListener {
            authState.postValue(TransactionState.SUCCESS)
        }
    }
}