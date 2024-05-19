package com.software3.paws_hub_android.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.FragmentPetFinderBinding
import com.software3.paws_hub_android.viewmodel.MainActivityViewModel
import com.software3.paws_hub_android.viewmodel.PetFinderViewModel

class PetFinderFragment : Fragment() {
    private var _binding: FragmentPetFinderBinding? = null
    private val binding  get() = _binding!!
    private val viewModel: PetFinderViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetFinderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}