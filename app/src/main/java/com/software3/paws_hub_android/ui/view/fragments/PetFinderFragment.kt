package com.software3.paws_hub_android.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.FragmentPetFinderBinding
import com.software3.paws_hub_android.model.Post
import com.software3.paws_hub_android.ui.adapters.PetFinderAdapter
import com.software3.paws_hub_android.ui.adapters.PostAdapter
import com.software3.paws_hub_android.ui.view.activities.PostDetailActivity
import com.software3.paws_hub_android.ui.view.dialogs.FilterDialog
import com.software3.paws_hub_android.viewmodel.MainActivityViewModel
import com.software3.paws_hub_android.viewmodel.PetFinderViewModel

class PetFinderFragment : Fragment() {
    private var _binding: FragmentPetFinderBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PetFinderViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetFinderBinding.inflate(inflater, container, false)
        initUI()
        initObservers()
        initListeners()
        viewModel.fetchPosts()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onCreateOptionsMenu(menu, inflater)", "androidx.fragment.app.Fragment")
    )
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_discover_menu, menu)
    }

    @Deprecated(
        "Deprecated in Java",
        ReplaceWith("super.onCreateOptionsMenu(menu, inflater)", "androidx.fragment.app.Fragment")
    )
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id._menu_item_filter_posts -> {
            viewModel.onFiltersItemClick()
            true
        }
        else -> {
            false
        }
    }

    private fun initUI() {
        val manager = LinearLayoutManager(requireContext())
        val declaration = DividerItemDecoration(requireContext(), manager.orientation)
        binding.rcvPetFinderPosts.layoutManager = manager
        binding.rcvPetFinderPosts.addItemDecoration(declaration)
    }

    private fun initObservers() {
        viewModel.locations.observe(viewLifecycleOwner) { locations ->
            locations?.let { onCreateFiltersDialog(it) }
        }
        viewModel.posts.observe(viewLifecycleOwner) {
            val adapter = PetFinderAdapter(
                list = it,
                onViewClick = this::onBtnViewClick
            )
            binding.sprLayoutPosts.isRefreshing = false
            binding.rcvPetFinderPosts.adapter = adapter
        }
    }

    private fun initListeners() {

    }

    private fun updateUI() {

    }

    private fun onCreateFiltersDialog(filters: List<String>) {
        FilterDialog(requireContext()) {
            viewModel.onFilterDialogCancel()
        }.setItems(filters.toTypedArray()) {
            viewModel.setLocationFilter(filters[it])
            viewModel.fetchPosts()
        }.show()
    }

    private fun onBtnViewClick(post: Post) {
        Intent(requireContext(), PostDetailActivity::class.java).also {
            it.putExtra("postID", post.postID)
            startActivity(it)
        }
    }
}