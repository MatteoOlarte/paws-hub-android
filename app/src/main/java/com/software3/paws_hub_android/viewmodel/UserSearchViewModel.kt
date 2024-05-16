package com.software3.paws_hub_android.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.model.dal.entity.user.ProfileDAl
import com.software3.paws_hub_android.ui.viewstate.TransactionViewState as ViewState


class UserSearchViewModel: ViewModel() {
    private val _profilesResult = MutableLiveData<List<Profile>>()
    val profilesResult: LiveData<List<Profile>> = _profilesResult

    private val _selectedProfile = MutableLiveData<Profile?>(null)
    val selectedProfile: LiveData<Profile?> = _selectedProfile

    private val _viewState = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState

    fun onProfileSearch(value: String) {
        _viewState.value = ViewState(isPending = true)
        viewModelScope.launch(Dispatchers.IO) {
            val result = ProfileDAl().filterByUsernameContains(value)
            val profiles = result.result
            _viewState.value = ViewState(isSuccess = true)
            _profilesResult.postValue(profiles)
        }
    }

    fun onItemSelected(profile: Profile?) {
        _selectedProfile.value = profile
    }
}