package com.software3.paws_hub_android.core.ex

import android.net.Uri

fun String?.isEmailAddress(): Boolean {
    if (this == null) {
        return false
    }
    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    return emailRegex.matches(this)
}

fun String.toURI(): Uri {
    return Uri.parse(this)
}