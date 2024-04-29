package com.software3.paws_hub_android.view.main_activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.ActivityMainBinding
import com.software3.paws_hub_android.view.WelcomeActivity
import com.software3.paws_hub_android.viewmodel.UserViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        navController = (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
        initUI()
        initObservers()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        userViewModel.checkUserLoggedInStatus()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.activity_main__logout -> {
            userViewModel.logoutCurrentUser()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

    private fun initUI() {
        val toolbarConfig = AppBarConfiguration(
            setOf(
                R.id.discoverFragment,
                R.id.profileFragment,
                R.id.searchFragment,
                R.id.petFinderFragment
            )
        )
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, toolbarConfig)
        binding.navigationBar.setupWithNavController(navController)
    }

    private fun initObservers() {
        userViewModel.isGuestUser.observe(this) { isGuestUser ->
            isGuestUser?.let { if (it) navigateToWelcomeActivity() }
        }
    }

    private fun initListeners() {
        navController.addOnDestinationChangedListener {_, _, _ ->
            binding.appbarLayout.setExpanded(true)
        }
    }

    private fun navigateToWelcomeActivity() =
        Intent(this.applicationContext, WelcomeActivity::class.java).also {
            startActivity(it)
        }
}