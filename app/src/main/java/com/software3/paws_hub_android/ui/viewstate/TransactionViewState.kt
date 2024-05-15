package com.software3.paws_hub_android.ui.viewstate

data class TransactionViewState (
    val isSuccess: Boolean = false,
    val isPending: Boolean = false,
    val isFailure: Boolean = false,
    val error: String? = null
)