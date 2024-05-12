package com.software3.paws_hub_android.ui.viewstate


data class PetCreatorViewState(
    val isEmpty: Boolean? = true,
    val isValidName: Boolean? = null,
    val isValidBirthDate: Boolean? = null,
    val isValidWeight: Boolean? = null,
    val isValidPetType: Boolean? = null,
    val isValidPetBreed: Boolean? = null,
) {
    val isFormValid
        get() = isValidName == true && isValidBirthDate == true && isValidWeight == true && isValidPetType == true && isValidPetBreed == true
}
