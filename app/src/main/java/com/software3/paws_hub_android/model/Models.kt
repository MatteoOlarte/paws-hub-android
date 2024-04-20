package com.software3.paws_hub_android.model

data class UserData(
    val _id: String,
    var fName: String,
    var lName: String,
    var uName: String,
    var city: String?,
    var photo: String?,
    var email: String?,
    var phoneNumber: String?
)

data class Post(
    val _id: String
)