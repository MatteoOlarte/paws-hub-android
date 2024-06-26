package com.software3.paws_hub_android.model

import android.net.Uri
import java.util.Date


data class Profile(
    val profileID: String,
    var fName: String,
    var lName: String,
    var uName: String,
    var pets: MutableList<String> = mutableListOf(),
    var city: String? = null,
    var photo: Uri? = null,
    var email: String? = null,
    var phoneNumber: String? = null,
    var preferredPet: String? = null,
) {
    val fullName get() = "$fName $lName"
}

data class UserSignIn(
    var email: String?, var phone: String?
)

data class Post(
    val postID: String,
    val image: Uri,
    val body: String,
    val created: Date,
    val type: String?,
    val location: String? = null,
    val author: Map<String, String?>,
    val pet: Map<String, String?>
)

data class Pet(
    val petID: String,
    var name: String,
    var weight: Float,
    var notes: String?,
    val birthDate: Date,
    val ownerID: String,
    val typeID: String?,
    val breed: Map<String, String?>?
)

data class PetType(
    val typeID: String
)

data class PetBreed(
    val breedID: String,
    val breedName: String,
    val typeID: String
)

data class PetPublish(
    val name: String,
    val weight: Float,
    val breed: String,
    val notes: String?,
    val alternativeBreed: String? = null
)
