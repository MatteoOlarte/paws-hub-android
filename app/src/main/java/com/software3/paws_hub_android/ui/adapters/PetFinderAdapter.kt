package com.software3.paws_hub_android.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.LayoutPostDiscoverBinding
import com.software3.paws_hub_android.model.Post

class PetFinderAdapter(
    private val list: List<Post>,
    onViewClick: (post: Post) -> Unit = {},
    onSaveClick: (post: Post) -> Unit = {}
) : PostAdapter(list, onViewClick, {}, onSaveClick) {
    inner class ViewHolder(view: View) : PostAdapter.ViewHolder(view) {
        private val biding = LayoutPostDiscoverBinding.bind(view)

        @SuppressLint("SetTextI18n")
        override fun render(
            item: Post,
            onViewClick: (post: Post) -> Unit,
            onLikeClick: (post: Post) -> Unit,
            onSaveClick: (post: Post) -> Unit
        ) {
            super.render(item, onViewClick, onLikeClick, onSaveClick)
            biding.cardTvLastLocation.visibility = View.VISIBLE
            biding.tvLastLocation.text = "${item.location}, Bogotá.DC"
            biding.btnLikePost.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.layout_post_discover, parent, false))
    }

    override fun onBindViewHolder(holder: PostAdapter.ViewHolder, position: Int) {
        val item = list[position]
        val holder2 = holder as PetFinderAdapter.ViewHolder
        holder2.render(item, super.onViewClick, super.onLikeClick, super.onSaveClick)
    }
}