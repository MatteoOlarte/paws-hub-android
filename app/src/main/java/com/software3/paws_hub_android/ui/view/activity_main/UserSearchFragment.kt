package com.software3.paws_hub_android.ui.view.activity_main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.FragmentProfileSearchBinding
import com.software3.paws_hub_android.model.Profile
import com.software3.paws_hub_android.ui.adapters.ProfileSearchAdapter
import com.software3.paws_hub_android.ui.viewstate.TransactionViewState
import com.software3.paws_hub_android.viewmodel.MainActivityViewModel
import com.software3.paws_hub_android.viewmodel.UserSearchViewModel
import kotlinx.coroutines.launch


class UserSearchFragment : Fragment() {
    private lateinit var searchView: SearchView
    private var _binding: FragmentProfileSearchBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel: MainActivityViewModel by activityViewModels()
    private val viewmodel: UserSearchViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileSearchBinding.inflate(inflater, container, false)
        initUI()
        initObservers()
        initListeners()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Deprecated("Deprecated in Java", ReplaceWith("inflater.inflate(R.menu.fragment_search_menu, menu)", "com.software3.paws_hub_android.R"))
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_search_menu, menu)
        val menuItem: MenuItem = menu.findItem(R.id.menu_item_search)
        searchView = menuItem.actionView as SearchView
        searchView.queryHint = getString(R.string.deceptive_title_lost_pets)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = onSearchSubmit(query)
            override fun onQueryTextChange(newText: String): Boolean = false
        })
    }

    private fun initUI() {
        val manager = LinearLayoutManager(requireContext())
        val declaration = DividerItemDecoration(requireContext(), manager.orientation)
        binding.rcvSearchResults.layoutManager = manager
        binding.rcvSearchResults.addItemDecoration(declaration)
    }

    private fun initObservers() {
        viewmodel.profilesResult.observe(viewLifecycleOwner) {
            binding.rcvSearchResults.adapter = ProfileSearchAdapter(it, viewmodel::onItemSelected)
        }
        viewmodel.selectedProfile.observe(viewLifecycleOwner) {profile ->
            profile?.let {
                Toast.makeText(requireContext(), it.uName, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_searchFragment_to_profileViewerFragment)
                viewmodel.onItemSelected(null)
            }
        }
    }

    private fun initListeners() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewmodel.viewState.collect { updateUI(it) }
            }
        }
    }

    private fun onSearchSubmit(query: String): Boolean {
        viewmodel.onProfileSearch(query)
        return true
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

    private fun onSuccess() {
        activityViewModel.stopProgressIndicator()
    }

    private fun onPending() {
        activityViewModel.startProgressIndicator()
    }

    private fun onFailure(error: String) {

    }
}