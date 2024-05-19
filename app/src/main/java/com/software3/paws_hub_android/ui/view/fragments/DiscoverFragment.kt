package com.software3.paws_hub_android.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.software3.paws_hub_android.ui.adapters.PostAdapter
import com.software3.paws_hub_android.databinding.FragmentDiscoverBinding
import com.software3.paws_hub_android.viewmodel.DiscoverViewModel


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

    private fun initUI() {
        val manager = LinearLayoutManager(requireContext())
        val declaration = DividerItemDecoration(requireContext(), manager.orientation)
        binding.recyclerViewPosts.layoutManager = manager
        binding.recyclerViewPosts.addItemDecoration(declaration)
    }

    private fun initObservers() {
        viewmodel.posts.observe(viewLifecycleOwner) {
            binding.sprLayoutDiscoverPosts.isRefreshing = false
            binding.recyclerViewPosts.adapter = PostAdapter(it)
        }
    }

    private fun initListeners() {
        binding.sprLayoutDiscoverPosts.setOnRefreshListener {
            viewmodel.fetchAllPosts()
        }
    }
}