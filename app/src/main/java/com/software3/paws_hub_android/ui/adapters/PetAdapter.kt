package com.software3.paws_hub_android.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.core.ex.getStringResource
import com.software3.paws_hub_android.databinding.LayoutPetItemBinding
import com.software3.paws_hub_android.model.Pet
import java.text.SimpleDateFormat


open class PetAdapter(private val list: List<Pet>) : RecyclerView.Adapter<PetAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.layout_pet_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.render(item, if (position > 0) 8 else 0)
    }

    override fun getItemCount(): Int = list.size

    open inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        protected val binding = LayoutPetItemBinding.bind(view)

        @SuppressLint("SetTextI18n")
        fun render(pet: Pet, gap: Int = 0) {
            binding.tvPetName.text = pet.name
            binding.tvPetBirthDate.text = SimpleDateFormat.getDateInstance().format(pet.birthDate)
            binding.tvPetWeight.text = "${pet.weight} KG"
            if (pet.typeID == "type_other") {
                binding.tvPetType.text = "${pet.breed?.get("name")}"
            } else {
                binding.tvPetType.text = "${pet.typeID?.getStringResource(view.context)}, ${pet.breed?.get("name")}"
            }
        }
    }
}