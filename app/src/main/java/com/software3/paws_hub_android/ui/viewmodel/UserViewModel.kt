package com.software3.paws_hub_android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.model.dal.entity.pet.PetDAl
import com.software3.paws_hub_android.model.dal.entity.user.UserDataDAL


class UserViewModel : ViewModel() {
    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private val _pets = MutableLiveData<List<Pet>>()
    val pets: LiveData<List<Pet>> = _pets

    private val _userdata = MutableLiveData<Profile>()
    val userdata: LiveData<Profile> = _userdata

    private val _isGuestUser = MutableLiveData<Boolean>()
    val isGuestUser: LiveData<Boolean> = _isGuestUser

    private val transactionState = MutableLiveData<TransactionState>()


    fun fetchUserData() {
        val user = currentUser
        val dal = UserDataDAL()

        if (user == null) {
            transactionState.postValue(TransactionState.FAILED)
            return
        }
        dal.get(user.uid).addOnSuccessListener {
            dal.cast(it).also { data ->
                _userdata.postValue(data)
                transactionState.postValue(TransactionState.SUCCESS)
            }
        }.addOnFailureListener {
            transactionState.postValue(TransactionState.FAILED)
        }
    }

    fun fetchUserPets(data: Profile) {
        val petsIDs: List<String> = data.pets.toList()
        val petDAL = PetDAl()

        petDAL.filterByOwner(petsIDs) { _pets.postValue(it) }
    }

    fun logoutCurrentUser() {
        FirebaseAuth.getInstance().signOut()
        _isGuestUser.postValue(true)
    }

    fun checkUserLoggedInStatus() = _isGuestUser.postValue(currentUser == null)
}