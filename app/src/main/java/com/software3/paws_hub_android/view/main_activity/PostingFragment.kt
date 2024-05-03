package com.software3.paws_hub_android.view.main_activity

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.FragmentPostingBinding
import com.software3.paws_hub_android.model.UserData
import com.software3.paws_hub_android.viewmodel.MainActivityViewModel
import com.software3.paws_hub_android.viewmodel.PostViewModel
import com.squareup.picasso.Picasso


class PostingFragment : Fragment() {
    private var _biding: FragmentPostingBinding? = null
    private val binding get() = _biding!!
    private val viewmodel: PostViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by activityViewModels()
    private val imageResult = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewmodel.setPostImage(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userdata: UserData? = mainActivityViewModel.userdata.value
        _biding = FragmentPostingBinding.inflate(inflater, container, false)
        initUI()
        initObservers()
        initListeners()
        userdata?.let { viewmodel.fetchUserPets(it) }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _biding = null
    }

    private fun initUI() {
        binding.tfLayoutPostPet.isEnabled = false
    }

    private fun initObservers() {
        viewmodel.postImageUri.observe(viewLifecycleOwner) {
            Picasso.get().load(it).into(binding.imgPostPhoto)
        }
        viewmodel.pets.observe(viewLifecycleOwner) { pets ->
            val items: List<String> = pets.map { it.name }.toList()
            setTfPostPetItems(items)
            binding.tfLayoutPostPet.isEnabled = true
        }
        viewmodel.selectedPet.observe(viewLifecycleOwner) {
            mainActivityViewModel.createSimpleSnackbarMessage("Mascota Cargada")
        }
        viewmodel.publishState.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initListeners() {
        binding.cardPostPhoto.setOnClickListener { imageResult.launch("image/*") }
        binding.btnPublishPost.setOnClickListener { onPublish() }
        binding.tfPostPet.setOnItemClickListener { _, _, position, _ ->
            viewmodel.setSelectedPet(position)
        }
    }

    private fun setTfPostPetItems(items: List<String>) {
        val adapter = ArrayAdapter(requireContext(), R.layout.layout_list_adapter, items)
        with(binding) { tfPostPet.setAdapter(adapter) }
    }

    private fun onPublish() {
        val userdata: UserData? = mainActivityViewModel.userdata.value
        userdata?.let { viewmodel.publishPost(it, binding.tfPostBody.text.toString()) }
    }
}