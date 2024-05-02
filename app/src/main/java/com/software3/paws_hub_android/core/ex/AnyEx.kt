package com.software3.paws_hub_android.core.ex

inline fun <reified T> Any.toListOrEmpty(): List<T> {
    return (this as? List<*>)?.filterIsInstance<T>() ?: emptyList()
}