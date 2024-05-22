package com.software3.paws_hub_android.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.Post
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.model.dal.entity.pet.PetDAl
import com.software3.paws_hub_android.model.dal.entity.post.PostDAl
import com.software3.paws_hub_android.model.dal.entity.user.CityObject
import com.software3.paws_hub_android.model.dal.storage.CloudStorage
import com.software3.paws_hub_android.ui.viewstate.TransactionViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Date
import java.util.UUID


class PostingViewModel : ViewModel() {
    private val _locations = MutableLiveData<List<String>>()
    val locations: LiveData<List<String>> = _locations

    private val _pets = MutableLiveData<List<Pet>>()
    val pets: LiveData<List<Pet>> = _pets

    private val _imageUri = MutableLiveData<Uri>()
    val postImageUri: LiveData<Uri> = _imageUri

    private val _isValidPet = MutableLiveData<Boolean>()
    val isValidPet: LiveData<Boolean> = _isValidPet

    private val _isValidBody = MutableLiveData<Boolean>()
    val isValidBody: LiveData<Boolean> = _isValidBody

    private val _isValidLoc = MutableLiveData<Boolean>()
    val isValidLoc: LiveData<Boolean> = _isValidLoc

    private val _isValidForm = MutableLiveData<Boolean>()
    val isValidForm: LiveData<Boolean> = _isValidForm

    private val _pet = MutableLiveData<Pet>()
    val pet: LiveData<Pet> = _pet

    private val _viewState = MutableStateFlow(TransactionViewState())
    val viewState: StateFlow<TransactionViewState> = _viewState


    fun fetchLocations() {
        _locations.value = CityObject().getAll()
    }

    fun fetchPetsFromUser(data: Profile) {
        val petsIDs: List<String> = data.pets.toList()
        viewModelScope.launch(Dispatchers.IO) {
            val pets = PetDAl().filterByIDin(petsIDs)

            if (pets.error == null) {
                _pets.postValue(pets.result)
            } else {
                _viewState.value = TransactionViewState(
                    isFailure = true,
                    error = ""
                )
            }
        }
    }

    fun setImage(uri: Uri) {
        _imageUri.value = uri
        validateForm()
    }

    fun setPet(position: Int) {
        val pets = _pets.value
        pets?.let {
            _pet.postValue(it[position])
            _isValidPet.value = true
            validateForm()
        }
    }

    fun onBodyChanged(value: String) {
        _isValidBody.value = value.isNotEmpty()
        validateForm()
    }

    private fun validateForm() {
        val validate = (_imageUri.value != null && _pet.value != null && _isValidBody.value == true)
        Log.d("PostViewModel", "${_imageUri.value}")
        Log.d("PostViewModel", "${_pet.value != null}")
        Log.d("PostViewModel", "${_isValidBody.value == true}")
        _isValidForm.postValue(validate)
    }

    fun publishPetFinderPost(userdata: Profile, body: String, location: String) {
        val postID = UUID.randomUUID().toString()
        val postImage = _imageUri.value
        val postPet = _pet.value

        if (postImage == null || postPet == null) {
            _viewState.value = TransactionViewState(
                isFailure = true,
                error = "",
            )
            return
        }
        _viewState.value = TransactionViewState(isPending = true)
        viewModelScope.launch(Dispatchers.IO) {
            val downloadURL = trySavePhotoInStorage(postImage, postID)

            if (downloadURL == null) {
                _viewState.value = TransactionViewState(
                    isFailure = true,
                    error = ""
                )
                return@launch
            }
            val date = Date()
            val author = mapOf(
                "_id" to userdata.profileID,
                "full_name" to userdata.fullName,
                "username" to userdata.uName,
                "profile_photo" to userdata.photo.toString()
            )
            val pet = mapOf(
                "_id" to postPet.petID,
                "type" to postPet.typeID,
                "breed" to postPet.breed?.get("name"),
                "name" to postPet.name
            )
            val post = Post(
                postID = postID,
                image = downloadURL,
                body = body,
                created = date,
                author = author,
                pet = pet,
                type = "TYPE_FINDER",
                location = location
            )
            val query = PostDAl().save(post)

            if (query.result) {
                _viewState.value = TransactionViewState(isSuccess = true)
            } else {
                _viewState.value = TransactionViewState(
                    isFailure = true,
                    error = query.error?.message
                )
                Log.d("ViewModelScope", query.error?.message.toString())
            }
        }
    }

    fun publishPost(userdata: Profile, body: String) {
        val postID = UUID.randomUUID().toString()
        val postImage = _imageUri.value
        val postPet = _pet.value

        if (postImage == null || postPet == null) {
            _viewState.value = TransactionViewState(
                isFailure = true,
                error = "",
            )
            return
        }
        _viewState.value = TransactionViewState(isPending = true)
        viewModelScope.launch(Dispatchers.IO) {
            val downloadURL = trySavePhotoInStorage(postImage, postID)

            if (downloadURL == null) {
                _viewState.value = TransactionViewState(
                    isFailure = true,
                    error = ""
                )
                return@launch
            }
            val date = Date()
            val author = mapOf(
                "_id" to userdata.profileID,
                "full_name" to userdata.fullName,
                "username" to userdata.uName,
                "profile_photo" to userdata.photo.toString()
            )
            val pet = mapOf(
                "_id" to postPet.petID,
                "type" to postPet.typeID,
                "breed" to postPet.breed?.get("name"),
                "name" to postPet.name
            )
            val post = Post(
                postID = postID,
                image = downloadURL,
                body = body,
                created = date,
                author = author,
                pet = pet,
                type = "TYPE_DISCOVER"
            )
            val query = PostDAl().save(post)

            if (query.result) {
                _viewState.value = TransactionViewState(isSuccess = true)
            } else {
                _viewState.value = TransactionViewState(
                    isFailure = true,
                    error = query.error?.message
                )
            }
        }
    }

    private suspend fun trySavePhotoInStorage(uri: Uri?, postID: String): Uri? {
        if (uri == null) {
            return null
        }
        val cloudStorage = CloudStorage(uri, postID)
            .child("post")
            .child("images")
        return try {
            val result = cloudStorage.saveAsync()
            result.storage.downloadUrl.await()
        } catch (ex: Exception) {
            null
        }
    }
}
//165+