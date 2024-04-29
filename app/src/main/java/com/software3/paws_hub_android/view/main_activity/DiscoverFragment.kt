package com.software3.paws_hub_android.view.main_activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.FragmentDiscoverBinding


class DiscoverFragment : Fragment() {
    private var _biding: FragmentDiscoverBinding? = null
    private val binding get() = _biding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _biding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _biding = null
    }
}