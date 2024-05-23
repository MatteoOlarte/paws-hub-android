package com.software3.paws_hub_android.ui.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigationrail.NavigationRailView
import com.google.android.material.snackbar.Snackbar
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.ActivityMainBinding
import com.software3.paws_hub_android.ui.view.fragments.PetFinderFragmentDirections
import com.software3.paws_hub_android.viewmodel.MainActivityViewModel
import com.software3.paws_hub_android.viewmodel.ProfileViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val profileViewModel: ProfileViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        navController = (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
        initUI()
        initObservers()
        initListeners()
        mainActivityViewModel.fetchUserdata()
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.checkUserLoggedInStatus()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.activity_main__logout -> {
            profileViewModel.logoutCurrentUser()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun initUI() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.discoverFragment,
                R.id.profileFragment,
                R.id.searchFragment,
                R.id.petFinderFragment
            )
        )
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        initNavigation()
    }

    private fun initNavigation() {
        val bottomNav = binding.navigationBar
        val drawerNav = binding.navigationBarDrawer

        bottomNav?.setupWithNavController(navController)
        drawerNav?.setupWithNavController(navController)
    }

    private fun initObservers() {
        profileViewModel.isGuestUser.observe(this) { isGuestUser ->
            isGuestUser?.let { if (it) navigateToWelcomeActivity() }
        }
        mainActivityViewModel.showProgressIndicator.observe(this) {
            binding.toolbarProgressIndicator.visibility = if (it) View.VISIBLE else View.GONE
            binding.appbarLayout.setExpanded(true)
        }
        mainActivityViewModel.simpleSnackbarMessage.observe(this) { showSnackbarMessage(it) }
    }

    private fun initListeners() {
        navController.addOnDestinationChangedListener { _, it, _ -> onDestinationChanged(it) }
        binding.floatingActionButton.setOnClickListener { onFloatingActionButtonClick() }
    }

    private fun showSnackbarMessage(massage: String) {
        val view = binding.coordinatorLayout
        Snackbar.make(this, view, massage, Snackbar.LENGTH_LONG).also {
            it.setAction("OK") { _ -> it.dismiss() }
            it.show()
        }
    }

    private fun onDestinationChanged(destination: NavDestination) {
        binding.appbarLayout.setExpanded(true)
        binding.floatingActionButton.visibility = when (destination.id) {
            R.id.discoverFragment -> View.VISIBLE
            R.id.petFinderFragment -> View.VISIBLE
            else -> View.INVISIBLE
        }
    }

    private fun onFloatingActionButtonClick() {
        with(navController) {
             when (currentDestination?.id) {
                R.id.discoverFragment -> navigate(R.id.action_discoverFragment_to_postingFragment)
                R.id.petFinderFragment -> navigate(PetFinderFragmentDirections.actionPetFinderFragmentToPostingFragment("TYPE_PET_FINDER"))
                else -> {}
            }
        }
    }

    private fun navigateToWelcomeActivity() =
        Intent(this.applicationContext, WelcomeActivity::class.java).also {
            startActivity(it)
        }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}