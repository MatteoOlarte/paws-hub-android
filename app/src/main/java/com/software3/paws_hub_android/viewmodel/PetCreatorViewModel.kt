package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.PetBreed
import com.software3.paws_hub_android.model.PetPublish
import com.software3.paws_hub_android.model.PetType
import com.software3.paws_hub_android.model.dal.entity.pet.PetBreedDAl
import com.software3.paws_hub_android.model.dal.entity.pet.PetDAl
import com.software3.paws_hub_android.model.dal.entity.pet.PetTypeDAl
import com.software3.paws_hub_android.model.dal.entity.user.ProfileDAl
import com.software3.paws_hub_android.ui.viewstate.PetCreatorViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID


class PetCreatorViewModel : ViewModel() {
    private val _petTypes = MutableLiveData<List<PetType>>()
    val petTypes: LiveData<List<PetType>> get() = _petTypes

    private val _petBreeds = MutableLiveData<List<PetBreed>>()
    val petBreeds: LiveData<List<PetBreed>> get() = _petBreeds

    private val _birthdate = MutableLiveData<Date?>()
    val birthdate: LiveData<Date?> get() = _birthdate

    private val _transactionState = MutableLiveData<TransactionState>()
    val transactionState: LiveData<TransactionState> get() = _transactionState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _viewState = MutableStateFlow(PetCreatorViewState())
    val viewState: StateFlow<PetCreatorViewState> get() = _viewState

    private val _allowOtherTypes = MutableLiveData<Boolean>()
    val allowOtherTypes: LiveData<Boolean> get() = _allowOtherTypes

    private var _selectedPetType: PetType? = null

    private var _selectedPetBreed: PetBreed? = null


    fun setBirthDate(date: Date?) {
        this._birthdate.value = date
    }

    fun setPetType(position: Int) {
        _petTypes.value?.let {
            _selectedPetType = it[position]

            if (_selectedPetType?.typeID == "type_other") {
                _selectedPetBreed = null
                _allowOtherTypes.postValue(true)
                onPetBreedFieldChanged()
            } else {
                _allowOtherTypes.postValue(false)
                fetchBreedsOf(_selectedPetType!!)
            }
        }
    }

    fun setPetBreed(position: Int) {
        _petBreeds.value?.let {
            _selectedPetBreed = it[position]
        }
    }

    fun fetchPetTypes() {
        val dal = PetTypeDAl()
        viewModelScope.launch(Dispatchers.IO) {
            val types = dal.getAll().result
            _petTypes.postValue(types)
        }
    }

    fun createPet(publish: PetPublish) {
        val type = _selectedPetType
        val birth = _birthdate.value
        val uuid = UUID.randomUUID().toString()
        val breed = _selectedPetBreed
        val userID = Firebase.auth.currentUser?.uid

        if (birth == null || userID == null) {
            _transactionState.postValue(TransactionState.FAILED)
            return
        }
        _transactionState.postValue(TransactionState.PENDING)
        viewModelScope.launch(Dispatchers.IO) {
            val pet = Pet(
                petID = uuid,
                name = publish.name,
                weight = publish.weight,
                typeID = type?.typeID,
                breed = mapOf("_id" to breed?.breedID, "name" to (breed?.breedName ?: publish.alternativeBreed)),
                notes = publish.notes,
                birthDate = birth,
                ownerID = userID,
            )
            val uploadResult = PetDAl().save(pet)
            val profileResult = ProfileDAl().get(userID)
            val profile = profileResult.result

            if (uploadResult.result && profile != null) {
                profile.pets.add(pet.petID)
                ProfileDAl().save(profile)
                _transactionState.postValue(TransactionState.SUCCESS)
            } else {
                _transactionState.postValue(TransactionState.FAILED)
                _errorMessage.postValue(uploadResult.error?.message)
            }
        }
    }

    fun onNameFieldChanged(name: String) {
        _viewState.value = PetCreatorViewState(
            isValidName = isValidName(name),
            isValidWeight = _viewState.value.isValidWeight,
            isValidBirthDate = _viewState.value.isValidBirthDate,
            isValidPetType = _viewState.value.isValidPetType,
            isValidPetBreed = _viewState.value.isValidPetBreed,
            isEmpty = false
        )
    }

    fun onDateFieldChanged(date: String) {
        _viewState.value = PetCreatorViewState(
            isValidName = _viewState.value.isValidName,
            isValidWeight = _viewState.value.isValidWeight,
            isValidBirthDate = isValidBirthDate(date),
            isValidPetType = _viewState.value.isValidPetType,
            isValidPetBreed = _viewState.value.isValidPetBreed,
            isEmpty = false
        )
    }

    fun onWeightFieldChanged(weight: String) {
        _viewState.value = PetCreatorViewState(
            isValidName = _viewState.value.isValidName,
            isValidWeight = isValidPetWeight(weight),
            isValidBirthDate = _viewState.value.isValidBirthDate,
            isValidPetType = _viewState.value.isValidPetType,
            isValidPetBreed = _viewState.value.isValidPetBreed,
            isEmpty = false
        )
    }

    fun onPetTypeFieldChanged() {
        _viewState.value = PetCreatorViewState(
            isValidName = _viewState.value.isValidName,
            isValidWeight = _viewState.value.isValidWeight,
            isValidBirthDate = _viewState.value.isValidBirthDate,
            isValidPetType = isValidPetType(),
            isValidPetBreed = _viewState.value.isValidPetBreed,
            isEmpty = false
        )
    }

    fun onPetBreedFieldChanged() {
        _viewState.value = PetCreatorViewState(
            isValidName = _viewState.value.isValidName,
            isValidWeight = _viewState.value.isValidWeight,
            isValidBirthDate = _viewState.value.isValidBirthDate,
            isValidPetType = _viewState.value.isValidPetType,
            isValidPetBreed = isValidPetBreed(),
            isEmpty = false
        )
    }

    fun onPetAlternativeBreedFieldChanged(breed: String) {
        if (_allowOtherTypes.value == true) {
            _viewState.value = PetCreatorViewState(
                isValidName = _viewState.value.isValidName,
                isValidWeight = _viewState.value.isValidWeight,
                isValidBirthDate = _viewState.value.isValidBirthDate,
                isValidPetType = _viewState.value.isValidPetType,
                isValidPetBreed = breed.isNotEmpty(),
                isEmpty = false
            )
        }
    }

    private fun fetchBreedsOf(type: PetType) {
        val dal = PetBreedDAl()
        viewModelScope.launch(Dispatchers.IO) {
            val breeds = dal.filterByType(type).result
            _petBreeds.postValue(breeds)
        }
    }

    private fun isValidName(name: String): Boolean = name.isNotEmpty()

    private fun isValidBirthDate(date: String) = _birthdate.value != null || date.isEmpty()

    private fun isValidPetType() = _selectedPetType != null

    private fun isValidPetBreed() = _selectedPetBreed != null

    private fun isValidPetWeight(weight: String) = try {
        weight.toFloat()
        true
    } catch (ex: Exception) {
        false
    }
}