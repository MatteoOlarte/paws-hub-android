package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.Post
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.model.dal.entity.pet.PetDAl
import com.software3.paws_hub_android.model.dal.entity.post.PostDAl
import com.software3.paws_hub_android.model.dal.entity.user.ProfileDAl
import com.software3.paws_hub_android.ui.viewstate.TransactionViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostDetailsViewModel : ViewModel() {
    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> = _post

    private val _pet = MutableLiveData<Pet>()
    val pet: LiveData<Pet> = _pet

    private val _ownerProfile = MutableLiveData<Profile>()
    val ownerProfile: LiveData<Profile> = _ownerProfile

    private val _viewState = MutableStateFlow(TransactionViewState())
    val viewState: StateFlow<TransactionViewState> get() = _viewState

    fun fetchPostByID(postID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.value = TransactionViewState(isPending = true)

            val result = PostDAl().get(postID)
            if (result.result != null) {
                _post.postValue(result.result)
                _viewState.value = TransactionViewState(isSuccess = true)
            } else {
                _viewState.value =
                    TransactionViewState(isFailure = true, error = result.error!!.message)
            }
        }
    }

    fun fetchPetByID(petID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.value = TransactionViewState(isPending = true)

            val result = PetDAl().get(petID)
            if (result.result != null) {
                _pet.postValue(result.result)
                _viewState.value = TransactionViewState(isSuccess = true)
            } else {
                _viewState.value =
                    TransactionViewState(isFailure = true, error = result.error!!.message)
            }
        }
    }

    fun fetchOwner(pet: Pet) {
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.value = TransactionViewState(isPending = true)

            val result = ProfileDAl().get(pet.ownerID)
            if (result.result != null) {
                _ownerProfile.postValue(result.result)
                _viewState.value = TransactionViewState(isSuccess = true)
            } else {
                _viewState.value =
                    TransactionViewState(isFailure = true, error = result.error!!.message)
            }
        }
    }
}