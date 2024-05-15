package com.software3.paws_hub_android.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.software3.paws_hub_android.model.Post
import com.software3.paws_hub_android.model.dal.entity.post.PostDAl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiscoverViewModel: ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    fun fetchAllPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val query = PostDAl().getAll()
            if (query.error == null) {
                _posts.postValue(query.result)
            } else {

            }
        }
    }
}