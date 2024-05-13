package com.software3.paws_hub_android.core.ex

inline fun <reified T> Any?.toListOrEmpty(): List<T> {
    return (this as? List<*>)?.filterIsInstance<T>() ?: emptyList()
}

inline fun <reified T> Any?.toMutableListOrEmpty(): MutableList<T> {
    return (this as? MutableList<*>)?.filterIsInstance<T>()?.toMutableList() ?: mutableListOf()
}

inline fun <reified T> Any?.toMapOrEmpty(): Map<String, T?> {
    val map = (this as? Map<*, *>)
        ?.filterKeys { it is String }
        ?.filterValues { it is T? }
        ?.map { it.key as String to it.value as T? }?.toMap() ?: mapOf()
    return map
}