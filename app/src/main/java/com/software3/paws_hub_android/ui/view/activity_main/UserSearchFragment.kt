package com.software3.paws_hub_android.ui.view.activity_main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.FragmentProfileSearchBinding


class UserSearchFragment : Fragment() {
    private var _binding: FragmentProfileSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}