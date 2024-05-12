package com.software3.paws_hub_android.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.LayoutPetItemBinding
import com.software3.paws_hub_android.model.Pet
import java.text.SimpleDateFormat


class PetAdapter(private val list: List<Pet>) : RecyclerView.Adapter<PetAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.layout_pet_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.render(item, if (position > 0) 8 else 0)
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = LayoutPetItemBinding.bind(view)

        fun render(pet: Pet, gap: Int = 0) {
            binding.txtViewPetName.text = pet.name
            binding.txtViewPetWeight.text = "${pet.weight} KG"
            binding.txtViewPetAge.text = SimpleDateFormat.getDateInstance().format(pet.birthDate)
            binding.cardContainer.layoutParams.apply { if (this is MarginLayoutParams) this.topMargin = gap }
        }
    }
}