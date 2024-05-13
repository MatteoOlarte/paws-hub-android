package com.software3.paws_hub_android.core.ex

import android.content.Context
import android.net.Uri
import com.software3.paws_hub_android.R

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

fun String.slugify(): String {
    return this.replace(" ", "_").lowercase()
}

fun String.getStringResource(context: Context): String? = when(this) {
    "type_dog" -> context.resources.getStringArray(R.array.pets_types)[0]
    "type_cat" -> context.resources.getStringArray(R.array.pets_types)[1]
    "type_other" -> context.resources.getStringArray(R.array.pets_types)[2]
    else -> null
}