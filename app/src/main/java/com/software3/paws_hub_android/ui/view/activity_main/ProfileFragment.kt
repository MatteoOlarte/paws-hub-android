package com.software3.paws_hub_android.ui.view.activity_main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.ui.adapters.PetAdapter
import com.software3.paws_hub_android.databinding.FragmentProfileBinding
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.ui.view.ProfileEditorActivity
import com.software3.paws_hub_android.viewmodel.MainActivityViewModel
import com.software3.paws_hub_android.viewmodel.UserViewModel
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
    private var _biding: FragmentProfileBinding? = null
    private val binding get() = _biding!!
    private val userViewModel: UserViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _biding = FragmentProfileBinding.inflate(inflater, container, false)
        initUI()
        initObservers()
        initListeners()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _biding = null
    }

    private fun initUI() {
        val userdata: Profile? = mainActivityViewModel.profileData.value
        binding.rcvPetsList.layoutManager = LinearLayoutManager(requireContext())
        userdata?.let {
            updateProfileCard(it)
            userViewModel.fetchUserPets(it)
        }
    }

    private fun initObservers() {
        userViewModel.pets.observe(viewLifecycleOwner) {
            binding.rcvPetsList.adapter = PetAdapter(it)
        }
    }

    private fun initListeners() {
        binding.btnEditProfile.setOnClickListener {
            Intent(this.context, ProfileEditorActivity::class.java).also { startActivity(it) }
        }
        binding.btnAddPet.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_petPublishFragment)
        }
    }

    private fun updateProfileCard(user: Profile) {
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