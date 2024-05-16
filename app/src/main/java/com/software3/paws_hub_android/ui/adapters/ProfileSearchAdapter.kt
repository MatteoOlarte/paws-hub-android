package com.software3.paws_hub_android.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.LayoutProfileItemBinding
import com.software3.paws_hub_android.model.Profile
import com.squareup.picasso.Picasso

class ProfileSearchAdapter(val list: List<Profile>, private val onClick: (Profile) -> Unit) : RecyclerView.Adapter<ProfileSearchAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val biding = LayoutProfileItemBinding.bind(view)
        private val picasso: Picasso = Picasso.get()

        fun render(item: Profile, onClick: (Profile) -> Unit) {
            biding.tvUsername.text = item.uName
            biding.tvFullName.text = item.fullName
            picasso.load(item.photo).into(biding.imgViewProfilePhoto)
            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.layout_profile_item, parent, false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Profile = list[position]
        holder.render(item, onClick)
    }
}