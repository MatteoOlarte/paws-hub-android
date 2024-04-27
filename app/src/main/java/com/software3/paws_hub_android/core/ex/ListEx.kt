package com.software3.paws_hub_android.core.ex

fun List<String?>.containsBlackOrNulls(): Boolean {
    for (f in this) {
        return f.isNullOrBlank()
    }
    return false
}