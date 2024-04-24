package com.software3.paws_hub_android.model

data class UserData(
    val _id: String = "",
    var fName: String = "",
    var lName: String = "",
    var uName: String = "@anonymous",
    var city: String? = null,
    var photo: String? = null,
    var email: String? = null,
    var phoneNumber: String? = null
)

data class Post(
    val _id: String
)