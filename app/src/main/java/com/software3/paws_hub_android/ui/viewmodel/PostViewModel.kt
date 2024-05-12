package com.software3.paws_hub_android.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.Post
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.model.dal.entity.pet.PetDAl
import com.software3.paws_hub_android.model.dal.entity.PostDAL
import com.software3.paws_hub_android.model.dal.storage.CloudStorage
import java.util.Date
import java.util.UUID

class PostViewModel: ViewModel() {
    private val _pets = MutableLiveData<List<Pet>>()
    val pets: LiveData<List<Pet>> = _pets

    private val _postImageUri = MutableLiveData<Uri>()
    val postImageUri: LiveData<Uri> = _postImageUri

    private val _publishState = MutableLiveData<TransactionState>()
    val publishState: LiveData<TransactionState> = _publishState

    private val _selectedPet = MutableLiveData<Pet>()
    val selectedPet: LiveData<Pet> = _selectedPet


    fun fetchUserPets(data: Profile) {
        val petsIDs: List<String> = data.pets.toList()
        val petDAL = PetDAl()
        petDAL.filterByOwner(petsIDs) { _pets.postValue(it) }
    }

    fun setPostImage(uri: Uri) {
        _postImageUri.postValue(uri)
    }

    fun setSelectedPet(position: Int) {
        val pets = _pets.value
        pets?.let {  _selectedPet.postValue(it[position]) }
    }

    fun publishPost(userdata: Profile, body: String) {
        val postID = UUID.randomUUID().toString()
        val postImage = _postImageUri.value
        val postPet = _selectedPet.value
        val postDAL = PostDAL()

        if (postImage == null || postPet == null) {
            _publishState.postValue(TransactionState.FAILED)
            return
        }
        trySavePhotoInStorage(postImage, postID) {
            if (it == null) {
                _publishState.postValue(TransactionState.FAILED)
                return@trySavePhotoInStorage
            }
            val date = Date()
            val post = Post(
                postID = postID,
                image = it,
                body = body,
                pupDate = date,
                authorFullName = userdata.fullName,
                authorUsername = userdata.uName,
                authorProfilePicture = userdata.photo,
                petName = postPet.name,
                authorID = userdata.profileID,
                petID = postPet.petID
            )
            postDAL.save(post).addOnSuccessListener {
                _publishState.postValue(TransactionState.SUCCESS)
            }.addOnFailureListener {
                _publishState.postValue(TransactionState.FAILED)
            }
        }
    }

    private fun trySavePhotoInStorage(uri: Uri?, postID: String, callback: (url: Uri?) -> Unit) {
        if (uri == null) {
            callback(null)
            return
        }
        val cloudStorage = CloudStorage(uri, postID)
        cloudStorage.child("post").child("images").save().addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { downloadURL -> callback(downloadURL)}
        }.addOnFailureListener {
            callback(null)
        }
    }
}