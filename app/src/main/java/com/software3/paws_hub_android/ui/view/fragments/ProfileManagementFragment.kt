package com.software3.paws_hub_android.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.FragmentProfileManagementBinding
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.ui.adapters.PetManagementAdapter
import com.software3.paws_hub_android.ui.view.activities.ProfileEditorActivity
import com.software3.paws_hub_android.ui.viewstate.TransactionViewState
import com.software3.paws_hub_android.viewmodel.MainActivityViewModel
import com.software3.paws_hub_android.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ProfileManagementFragment : Fragment() {
    private var _biding: FragmentProfileManagementBinding? = null
    private val binding get() = _biding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _biding = FragmentProfileManagementBinding.inflate(inflater, container, false)
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
        val userdata: Profile? = activityViewModel.profileData.value
        binding.rcvPetsList.layoutManager = LinearLayoutManager(requireContext())
        userdata?.let {
            updateProfileCard(it)
            profileViewModel.fetchUserPets(it)
        }
    }

    private fun initObservers() {
        profileViewModel.pets.observe(viewLifecycleOwner) {
            binding.rcvPetsList.adapter = PetManagementAdapter(it, onDelete = this::onItemDelete)
        }
        profileViewModel.onPetRemoved.observe(viewLifecycleOwner) {
            val pets = profileViewModel.pets.value
            pets?.let {list ->
                if (list.size > it) {
                    list.removeAt(it)
                    binding.rcvPetsList.adapter?.notifyItemRemoved(it)
                }
            }
        }
    }

    private fun updateUI(viewState: TransactionViewState) {
        if (viewState.isSuccess) {
            onSuccess()
            return
        }
        if (viewState.isPending) {
            onPending()
            return
        }
        if (viewState.isFailure) {
            onFailure(viewState.error!!)
            return
        }
    }

    private fun initListeners() {
        binding.btnEditProfile.setOnClickListener {
            Intent(this.context, ProfileEditorActivity::class.java).also { startActivity(it) }
        }
        binding.btnAddPet.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_petPublishFragment)
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                profileViewModel.viewState.collect { updateUI(it) }
            }
        }
    }

    private fun onItemDelete(pet: Pet): Boolean {
        profileViewModel.deletePet(pet)
        return true
    }

    private fun updateProfileCard(user: Profile) {
        with(binding.profileContainer) {
            Picasso.get().load(user.photo).into(profilePhoto)
            tvFullName.text = user.fullName
            txtViewUsername.text = user.uName
            txtViewCity.text = user.city
            txtViewPreferredPet.text = user.preferredPet
            txtViewCity.visibility = if (user.city.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
            txtViewPreferredPet.visibility = if (user.preferredPet.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun onSuccess() {
        activityViewModel.stopProgressIndicator()
    }

    private fun onPending() {
        activityViewModel.startProgressIndicator()
    }

    private fun onFailure(error: String) {
        activityViewModel.stopProgressIndicator()
        activityViewModel.createSimpleSnackbarMessage(error)
    }
}