package com.software3.paws_hub_android.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.dal.entity.PetObject

class PostViewModel: ViewModel() {
    private val _pets = MutableLiveData<List<Pet>>()
    val pets: LiveData<List<Pet>> = _pets

    private val _postImageUri = MutableLiveData<Uri>()
    val postImageUri: LiveData<Uri> = _postImageUri

    private val _publishState = MutableLiveData<TransactionState>()
    val publishState: LiveData<TransactionState> = _publishState

    fun fetchUserPets(data: UserData) {
        val petsIDs: List<String> = data.pets.toList()
        val petDAL = PetObject()
        petDAL.filterByOwner(petsIDs) { _pets.postValue(it) }
    }

    fun setPostImage(uri: Uri) {
        _postImageUri.postValue(uri)
    }

    fun publishPost() {

    }
}