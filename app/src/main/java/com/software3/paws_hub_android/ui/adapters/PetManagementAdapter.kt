package com.software3.paws_hub_android.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MenuRes
import androidx.appcompat.widget.PopupMenu
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.model.Pet

class PetManagementAdapter(
    private val list: List<Pet>,
    private val onClick: (pet: Pet) -> Unit = {},
    private val onDelete: (pet: Pet) -> Boolean = {false}
): PetAdapter(list) {
    open inner class ViewHolder(private val view: View): PetAdapter.ViewHolder(view) {
        fun render(pet: Pet, onClick: (pet: Pet) -> Unit, onDelete: (pet: Pet) -> Boolean) {
            super.render(pet = pet, gap = 0)
            itemView.setOnClickListener {
                onClick(pet)
            }
            itemView.setOnLongClickListener {
                val popup = showPopupMenu(view, R.menu.layout_pet_item_menu)
                popup.setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.menu_item_delete -> onDelete(pet)
                        else -> false
                    }
                }
                true
            }
        }

        private fun showPopupMenu(v: View, @MenuRes menuRes: Int): PopupMenu {
            val menu = PopupMenu(v.context, v)
            menu.menuInflater.inflate(menuRes, menu.menu)
            menu.show()
            return menu
        }
    }

    override fun onBindViewHolder(holder: PetAdapter.ViewHolder, position: Int) {
        val item = list[position]
        (holder as PetManagementAdapter.ViewHolder).render(item, onClick, onDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.layout_pet_item, parent, false))
    }
}