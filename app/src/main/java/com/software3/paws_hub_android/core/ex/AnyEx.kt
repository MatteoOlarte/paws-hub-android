package com.software3.paws_hub_android.core.ex

inline fun <reified T> Any?.toListOrEmpty(): List<T> {
    return (this as? List<*>)?.filterIsInstance<T>() ?: emptyList()
}

inline fun <reified T> Any?.toMutableListOrEmpty(): MutableList<T> {
    return (this as? MutableList<*>)?.filterIsInstance<T>()?.toMutableList() ?: mutableListOf()
}