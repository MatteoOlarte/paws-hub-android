package com.software3.paws_hub_android.ui.view.activity_main

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.FragmentPostingBinding
import com.software3.paws_hub_android.model.Pet
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.ui.viewstate.TransactionViewState
import com.software3.paws_hub_android.viewmodel.MainActivityViewModel
import com.software3.paws_hub_android.viewmodel.PostingViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class PostingFragment : Fragment() {
    private var _binding: FragmentPostingBinding? = null
    private val binding get() = _binding!!
    private val viewmodel: PostingViewModel by viewModels()
    private val activityViewmodel: MainActivityViewModel by activityViewModels()
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewmodel.setImage(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostingBinding.inflate(inflater, container, false)

        val profile: Profile? = activityViewmodel.profileData.value
        initUI()
        initObservers()
        initListeners()
        profile?.let { viewmodel.fetchPetsFromUser(it) }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        binding.tfLayoutPostPet.isEnabled = false
        binding.btnPublishPost.isEnabled = false
    }

    private fun initObservers() {
        viewmodel.postImageUri.observe(viewLifecycleOwner) { uri ->
            binding.imgPostPhotoIcon.visibility = View.GONE
            uri?.let { Picasso.get().load(it).into(binding.imgPostPhoto) }
        }
        viewmodel.pets.observe(viewLifecycleOwner) {
            binding.tfLayoutPostPet.isEnabled = true
            setTfPostPetItems(it)
        }
        viewmodel.pet.observe(viewLifecycleOwner) {
            val message = "Mascota Cargada"
            activityViewmodel.createSimpleSnackbarMessage(message)
        }
        viewmodel.isValidPet.observe(viewLifecycleOwner) {
            binding.tfLayoutPostPet.error = if (it) null else "error"
        }
        viewmodel.isValidBody.observe(viewLifecycleOwner) {
            binding.tfLayoutPostBody.error = if (it) null else "error"
        }
        viewmodel.isValidForm.observe(viewLifecycleOwner) {
            binding.btnPublishPost.isEnabled = it
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.viewState.collect { updateUI(it) }
            }
        }
    }

    private fun initListeners() {
        binding.cardPostPhoto.setOnClickListener { imageResult.launch("image/*") }
        binding.tfPostPet.setOnItemClickListener { _, _, position, _ ->
            viewmodel.setPet(position)
        }
        binding.tfPostBody.doOnTextChanged { _, _, _, _ ->
            viewmodel.onBodyChanged(binding.tfPostBody.text.toString())
        }
        binding.btnPublishPost.setOnClickListener{
            onPublishClick()
        }
    }

    private fun setTfPostPetItems(pets: List<Pet>) {
        val items: List<String> = pets.map { it.name }.toList()
        val adapter = ArrayAdapter(requireContext(), R.layout.layout_list_adapter, items)
        with(binding) { tfPostPet.setAdapter(adapter) }
    }

    private fun onPublishClick() {
        val userdata: Profile? = activityViewmodel.profileData.value
        userdata?.let { viewmodel.publishPost(it, binding.tfPostBody.text.toString()) }
    }

    private fun updateUI(state: TransactionViewState) {
        if (state.isSuccess) {
            onSuccess()
            return
        }
        if (state.isPending) {
            onPending()
            return
        }
        if (state.isFailure) {
            onFailure(state.error!!)
            return
        }
    }

    private fun onSuccess() {
        val msg = getString(R.string.pet_upload_success_message)
        activityViewmodel.stopProgressIndicator()
        activityViewmodel.createSimpleSnackbarMessage(msg)
        activityViewmodel.fetchUserdata()
        findNavController().navigateUp()
    }

    private fun onPending() {
        activityViewmodel.startProgressIndicator()
    }

    private fun onFailure(error: String) {
        val msg = getString(R.string.pet_upload_failure_message)
        activityViewmodel.stopProgressIndicator()
        activityViewmodel.createSimpleSnackbarMessage(msg)
    }
}