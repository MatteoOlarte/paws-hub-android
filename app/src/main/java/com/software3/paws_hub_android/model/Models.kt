package com.software3.paws_hub_android.model

import android.net.Uri
import com.google.type.DateTime

data class UserData(
    val _id: String,
    var fName: String,
    var lName: String,
    var uName: String,
    var city: String? = null,
    var photo: Uri? = null,
    var email: String? = null,
    var phoneNumber: String? = null,
    var preferredPet: String? = null
) {
    val fullName get() = "$fName $lName"
}

data class UserSignIn(
    var email: String?,
    var phone: String?
)

data class Post(
    val _id: String,
    val authorName: String,
    val authorUsername: String,
    val author: UserData,
    val image: Uri,
    val body: String,
    val pupDate: DateTime
)