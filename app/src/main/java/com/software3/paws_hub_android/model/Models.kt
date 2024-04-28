package com.software3.paws_hub_android.model

import android.net.Uri

data class UserData(
    val _id: String,
    var fName: String,
    var lName: String,
    var uName: String,
    var city: String? = null,
    var photo: Uri? = null,
    var email: String? = null,
    var phoneNumber: String? = null
)

data class UserSignIn(
    var email: String?,
    var phone: String?
)

data class Post(
    val _id: String
)