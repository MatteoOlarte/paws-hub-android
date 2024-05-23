package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software3.paws_hub_android.model.Post
import com.software3.paws_hub_android.model.dal.entity.post.PostDAl
import com.software3.paws_hub_android.model.dal.entity.user.CityObject
import com.software3.paws_hub_android.ui.viewstate.TransactionViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PetFinderViewModel : ViewModel() {
    private val _locations = MutableLiveData<List<String>?>()
    val locations: LiveData<List<String>?> get() = _locations

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _viewState = MutableStateFlow(TransactionViewState())
    val viewState: StateFlow<TransactionViewState> get() = _viewState

    private var _selectedLocation = "ALL"
    val selectedLocation get() = _selectedLocation

    fun onFilterDialogCancel() {
        _locations.value = null
    }

    fun onFiltersItemClick() {
        fetchAvailableLocations()
    }

    fun setLocationFilter(location: String) {
        _selectedLocation = location
        _locations.value = null
    }

    fun fetchPosts() {
        _viewState.value = TransactionViewState(isPending = true)

        viewModelScope.launch(Dispatchers.IO) {
            val filter = _selectedLocation
            val result = if (filter == "ALL") {
                PostDAl().filterByPostType("TYPE_FINDER")
            } else {
                PostDAl().filterByPostLocation(location = selectedLocation)
            }

            if (result.error == null) {
                _posts.postValue(result.result)
                _viewState.value = TransactionViewState(isSuccess = true)
            } else {
                _posts.postValue(result.result)
                _viewState.value = TransactionViewState(isFailure = true, error = result.error.message)
            }
        }
    }

    private fun fetchAvailableLocations() {
        val locations1: MutableList<String> = CityObject().getAll().toMutableList()
        locations1.add("ALL")
        _locations.value = locations1.toList()
    }

}