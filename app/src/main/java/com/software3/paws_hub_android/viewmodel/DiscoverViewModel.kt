package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software3.paws_hub_android.model.PetType
import com.software3.paws_hub_android.model.Post
import com.software3.paws_hub_android.model.dal.entity.pet.PetTypeDAl
import com.software3.paws_hub_android.model.dal.entity.post.PostDAl
import com.software3.paws_hub_android.ui.viewstate.TransactionViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DiscoverViewModel: ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _petTypes = MutableLiveData<List<PetType>?>()
    val petTypes: LiveData<List<PetType>?> get() = _petTypes

    private val _viewState = MutableStateFlow(TransactionViewState())
    val viewState: StateFlow<TransactionViewState> get() = _viewState

    private var _typeFilter: PetType? = null

    fun onFiltersItemClick() {
        fetchPetTypes()
    }

    fun setPetTypeFilter(type: PetType) {
        _typeFilter = type
        _petTypes.value = null
    }

    fun fetchAllPosts() {
        _viewState.value = TransactionViewState(isPending = true)

        viewModelScope.launch(Dispatchers.IO) {
            val filter = _typeFilter
            val result = if (filter == null) {
                PostDAl().getAll()
            } else {
                PostDAl().filterByPetType(filter.typeID)
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

    private fun fetchPetTypes() {
        val dal = PetTypeDAl()

        _viewState.value = TransactionViewState(isPending = true)
        viewModelScope.launch(Dispatchers.IO) {
            val types = dal.getAll().result
            _petTypes.postValue(types)
            _viewState.value = TransactionViewState(isSuccess = true)
        }
    }

    fun likePost() {
        TODO()
    }

    fun savePost() {
        TODO()
    }
}