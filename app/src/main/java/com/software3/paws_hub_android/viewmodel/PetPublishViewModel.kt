package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.core.ex.containsNulls
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.PetPublish
import com.software3.paws_hub_android.model.PetType
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.model.dal.entity.PetObject
import com.software3.paws_hub_android.model.dal.entity.PetTypeObject
import com.software3.paws_hub_android.model.dal.entity.UserDataObject
import java.util.Date
import java.util.UUID


class PetPublishViewModel : ViewModel() {

    val types = MutableLiveData<List<String>>()
    val breeds = MutableLiveData<PetType>()
    val state = MutableLiveData<TransactionState>()
    val birthdate = MutableLiveData<Date>()
    private var petType = MutableLiveData<String>()


    private fun fetchBreeds(typeID: String) {
        val dal = PetTypeObject()

        dal.get(typeID).addOnFailureListener {
            state.postValue(TransactionState.FAILED)
        }.addOnSuccessListener {
            val type: PetType = dal.cast(it)
            breeds.postValue(type)
        }
    }

    fun fetchTypes() {
        types.value = PetTypeObject().getSupportedPetTypes()
    }

    fun setBirthDate(date: Date) {
        this.birthdate.value = date
    }

    fun setPetType(item: Int) {
        petType.value = types.value?.get(item)
        petType.value?.let { fetchBreeds(it) }
    }

    fun publishPet(publish: PetPublish) {
        val dal = PetObject()
        val type: String? = petType.value
        val birth: Date? = birthdate.value
        val uuid = UUID.randomUUID().toString()
        val pet: Pet
        val userID = Firebase.auth.currentUser?.uid

        if (listOf(type, birth).containsNulls() || userID == null) {
            state.value = TransactionState.FAILED
            return
        }

        state.postValue(TransactionState.PENDING)
        pet = Pet(
            petID = uuid,
            name = publish.name,
            weight = publish.weight,
            type = type!!,
            breed = publish.breed,
            notes = publish.notes,
            birthDate = birth!!,
            ownerID = userID
        )
        dal.save(pet).addOnFailureListener {
            state.postValue(TransactionState.FAILED)
        }.addOnSuccessListener {
            saveUserData(pet)
        }
    }

    private fun saveUserData(pet: Pet) {
        val userDAL = UserDataObject()
        val userID: String? = Firebase.auth.currentUser?.uid

        if (userID == null) {
            state.postValue(TransactionState.FAILED)
            return
        }

        userDAL.get(userID).addOnSuccessListener {
            val userdata: UserData = userDAL.cast(it)
            userdata.pets.add(pet.petID)
            userDAL.save(userdata).addOnSuccessListener {
                state.postValue(TransactionState.SUCCESS)
            }.addOnFailureListener {
                state.postValue(TransactionState.FAILED)
            }
        }.addOnFailureListener {
            state.postValue(TransactionState.FAILED)
        }
    }
}