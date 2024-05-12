package com.software3.paws_hub_android.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.AdoptPostLayoutBinding
import com.software3.paws_hub_android.model.Post
import com.squareup.picasso.Picasso

class PostAdapter(val list: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val biding = AdoptPostLayoutBinding.bind(view)
        private val picasso: Picasso = Picasso.get()

        fun render(item: Post) {
            biding.tvAuthorFullName.text = item.authorFullName
            biding.tvPostBody.text = item.body
            biding.tvPetName.text = item.petName
            picasso.load(item.authorProfilePicture).into(biding.ivAuthorPhoto)
            picasso.load(item.image).into(biding.ivPostImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.adopt_post_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = list.size
}