package com.software3.paws_hub_android.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.LayoutPostDiscoverBinding
import com.software3.paws_hub_android.model.Post
import com.squareup.picasso.Picasso

class PostAdapter(val list: List<Post>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val biding = LayoutPostDiscoverBinding.bind(view)
        private val picasso: Picasso = Picasso.get()

        fun render(item: Post) {
            biding.tvFullName.text = item.author["full_name"]
            biding.tvUsername.text = item.author["username"]
            biding.tvBody.text = item.body

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
        holder.render(item)
    }

    override fun getItemCount(): Int = list.size
}