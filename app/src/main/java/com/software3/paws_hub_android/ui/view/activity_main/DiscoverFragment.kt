package com.software3.paws_hub_android.ui.view.activity_main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.software3.paws_hub_android.ui.adapters.PostAdapter
import com.software3.paws_hub_android.databinding.FragmentDiscoverBinding
import com.software3.paws_hub_android.ui.viewmodel.DiscoverViewModel


class DiscoverFragment : Fragment() {
    private var _biding: FragmentDiscoverBinding? = null
    private val binding get() = _biding!!
    private val viewmodel: DiscoverViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _biding = FragmentDiscoverBinding.inflate(inflater, container, false)
        initUI()
        initObservers()
        initListeners()
        viewmodel.fetchAllPosts()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _biding = null
    }

    fun initUI() {
        binding.recyclerViewPosts.layoutManager = LinearLayoutManager(requireContext())
    }

    fun initObservers() {
        viewmodel.posts.observe(viewLifecycleOwner) {
            binding.recyclerViewPosts.adapter = PostAdapter(it)
        }
    }

    fun initListeners() {

    }
}