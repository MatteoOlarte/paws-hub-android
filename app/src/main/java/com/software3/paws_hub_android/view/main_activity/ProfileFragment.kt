package com.software3.paws_hub_android.view.main_activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.FragmentProfileBinding
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.view.EditProfileActivity
import com.software3.paws_hub_android.viewmodel.UserViewModel
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
    private var _biding: FragmentProfileBinding? = null
    private val binding get() = _biding!!
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _biding = FragmentProfileBinding.inflate(inflater, container, false)
        initObservers()
        initListeners()
        userViewModel.fetchUserData()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _biding = null
    }

    private fun initObservers() {
        userViewModel.userdata.observe(viewLifecycleOwner) { it?.let(::updateProfileCard) }
    }

    private fun initListeners() {
        binding.btnEditProfile.setOnClickListener {
            Intent(this.context, EditProfileActivity::class.java).also { startActivity(it) }
        }
        binding.btnAddPet.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_petPublishFragment)
        }
    }

    private fun updateProfileCard(user: UserData) {
        with(binding.profileContainer) {
            Picasso.get().load(user.photo).into(profilePhoto)
            txtViewFullName.text = user.fullName
            txtViewUsername.text = user.uName
            txtViewCity.text = user.city
            txtViewPreferredPet.text = user.preferredPet
            txtViewCity.visibility = if (user.city.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
            txtViewPreferredPet.visibility = if (user.preferredPet.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
        }
    }
}