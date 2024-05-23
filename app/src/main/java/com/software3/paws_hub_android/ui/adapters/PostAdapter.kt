package com.software3.paws_hub_android.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.LayoutPostDiscoverBinding
import com.software3.paws_hub_android.model.Post
import com.squareup.picasso.Picasso

open class PostAdapter(
    private val list: List<Post>,
    protected val onViewClick: (post: Post) -> Unit = {},
    protected val onLikeClick: (post: Post) -> Unit = {},
    protected val onSaveClick: (post: Post) -> Unit = {}
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    open inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val biding = LayoutPostDiscoverBinding.bind(view)
        private val picasso: Picasso = Picasso.get()

        open fun render(
            item: Post,
            onViewClick: (post: Post) -> Unit,
            onLikeClick: (post: Post) -> Unit,
            onSaveClick: (post: Post) -> Unit
        ) {
            biding.tvFullName.text = item.author["full_name"]
            biding.tvUsername.text = item.author["username"]
            biding.tvBody.text = item.body
            biding.btnViewPost.setOnClickListener { onViewClick(item) }
            biding.btnLikePost.setOnClickListener { onLikeClick(item) }
            biding.btnSavePost.setOnClickListener { onSaveClick(item) }
            picasso.load(item.author["profile_photo"]).into(biding.imgProfilePhoto)
            picasso.load(item.image).into(biding.imgPostPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.layout_post_discover, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.render(item, onViewClick, onLikeClick, onSaveClick)
    }

    override fun getItemCount(): Int = list.size
}