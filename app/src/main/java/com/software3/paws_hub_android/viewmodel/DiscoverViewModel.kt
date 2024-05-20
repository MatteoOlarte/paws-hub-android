package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.Post
import com.software3.paws_hub_android.model.dal.entity.pet.PetDAl
import com.software3.paws_hub_android.model.dal.entity.post.PostDAl
import com.software3.paws_hub_android.ui.viewstate.TransactionViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DiscoverViewModel: ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _viewState = MutableStateFlow(TransactionViewState())
    val viewState: StateFlow<TransactionViewState> get() = _viewState

    fun fetchAllPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.value = TransactionViewState(isPending = true)

            val result = PostDAl().getAll()
            if (result.error == null) {
                _posts.postValue(result.result)
                _viewState.value = TransactionViewState(isSuccess = true)
            } else {
                _posts.postValue(result.result)
                _viewState.value = TransactionViewState(isFailure = true, error = result.error.message)
            }
        }
    }

    fun likePost() {
        TODO()
    }

    fun savePost() {
        TODO()
    }
}