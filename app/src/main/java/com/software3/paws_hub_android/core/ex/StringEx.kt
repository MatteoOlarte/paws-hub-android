package com.software3.paws_hub_android.core.ex

fun String?.isEmailAddress(): Boolean {
    if (this == null) {
        return false
    }
    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    return emailRegex.matches(this)
}