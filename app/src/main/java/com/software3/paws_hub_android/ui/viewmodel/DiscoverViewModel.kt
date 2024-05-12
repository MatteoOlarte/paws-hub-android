package com.software3.paws_hub_android.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.software3.paws_hub_android.model.Post
import com.software3.paws_hub_android.model.dal.entity.PostDAL

class DiscoverViewModel: ViewModel() {
    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    fun fetchAllPosts() {
        val postDAL = PostDAL()
        postDAL.getAll { posts -> posts?.let { _posts.postValue(it) } }
    }
}