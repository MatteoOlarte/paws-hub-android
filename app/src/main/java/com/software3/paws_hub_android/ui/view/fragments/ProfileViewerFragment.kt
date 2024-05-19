package com.software3.paws_hub_android.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.software3.paws_hub_android.databinding.FragmentProfileViewerBinding
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.ui.adapters.PetAdapter
import com.software3.paws_hub_android.ui.viewstate.TransactionViewState
import com.software3.paws_hub_android.viewmodel.MainActivityViewModel
import com.software3.paws_hub_android.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ProfileViewerFragment : Fragment() {
    private var _binding: FragmentProfileViewerBinding? = null
    private val binding get() = _binding!!
    private val args: ProfileViewerFragmentArgs by navArgs()
    private val viewmodel: ProfileViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileViewerBinding.inflate(inflater, container, false)
        initUI()
        initObservers()
        initListeners()
        viewmodel.fetchProfileData(args.profileID)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        binding.rcvPetsList.layoutManager = LinearLayoutManager(requireContext())
        (this.activity as? AppCompatActivity)?.supportActionBar?.title = ""
    }

    private fun initObservers() {
        viewmodel.profile.observe(viewLifecycleOwner) {
            onProfileDataLoaded(it)
            viewmodel.fetchUserPets(it)
        }
        viewmodel.pets.observe(viewLifecycleOwner) {
            binding.rcvPetsList.adapter = PetAdapter(it)
        }
    }

    private fun initListeners() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.viewState.collect { updateUI(it) }
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

    private fun onProfileDataLoaded(profile: Profile) {
        with(binding.profileContainer) {
            Picasso.get().load(profile.photo).into(profilePhoto)
            tvFullName.text = profile.fullName
            txtViewUsername.text = profile.uName
            txtViewCity.text = profile.city
            txtViewPreferredPet.text = profile.preferredPet
            txtViewCity.visibility = if (profile.city.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
            txtViewPreferredPet.visibility = if (profile.preferredPet.isNullOrBlank()) View.INVISIBLE else View.VISIBLE
        }
        (this.activity as? AppCompatActivity)?.supportActionBar?.title = profile.fullName
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