package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

class ProfileViewModel : ViewModel() {
    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private val _isGuestUser = MutableLiveData<Boolean>()
    val isGuestUser: LiveData<Boolean> = _isGuestUser

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

    private val _pets = MutableLiveData<MutableList<Pet>>()
    val pets: LiveData<MutableList<Pet>> = _pets

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _onPetRemoved = MutableLiveData<Int>()
    val onPetRemoved: LiveData<Int> = _onPetRemoved

    private val _viewState = MutableStateFlow(TransactionViewState())
    val viewState: StateFlow<TransactionViewState> = _viewState

    fun fetchProfileData() {
        val user: FirebaseUser? = currentUser
        if (user == null) {
            _viewState.value = TransactionViewState(isFailure = true, error = "")
            return
        }
        fetchProfileData(user.uid)
    }

    fun fetchProfileData(profileID: String) {
        _viewState.value = TransactionViewState(isPending = true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = ProfileDAl().get(profileID)

            if (result.result != null) {
                _profile.postValue(result.result)
                _viewState.value = TransactionViewState(isSuccess = true)
            } else {
                _viewState.value = TransactionViewState(isFailure = true, error = "")
            }
        }
    }

    fun fetchUserPets(data: Profile) {
        val petsIDs: List<String> = data.pets.toList()

        _viewState.value = TransactionViewState(isPending = true)
        viewModelScope.launch(Dispatchers.IO) {
            val pets = PetDAl().filterByIDin(petsIDs)
            _pets.postValue(pets.result.toMutableList())
            if (pets.error == null) {
                _viewState.value = TransactionViewState(isSuccess = true)
            } else {
                _viewState.value = TransactionViewState(
                    isFailure = true,
                    error = pets.error.message
                )
            }
        }
    }

    fun fetchUserPosts(data: Profile) {
        TODO()
    }

    fun deletePet(pet: Pet) {
        val petsList = this.pets.value
        petsList?.let {
            val pos = it.indexOf(pet)

            _viewState.value = TransactionViewState(isPending = true)
            viewModelScope.launch(Dispatchers.IO) {
                val result = PetDAl().delete(pet)

                if (result.result) {
                    _onPetRemoved.postValue(pos)
                    _viewState.value = TransactionViewState(isSuccess = true)
                } else {
                    _viewState.value = TransactionViewState(
                        isFailure = true,
                        error = result.error?.message.toString()
                    )
                }
            }
        }
    }

    fun logoutCurrentUser() {
        FirebaseAuth.getInstance().signOut()
        _isGuestUser.postValue(true)
    }

    fun checkUserLoggedInStatus() = _isGuestUser.postValue(currentUser == null)
}