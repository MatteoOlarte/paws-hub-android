package com.software3.paws_hub_android.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.commit
import com.google.android.material.elevation.SurfaceColors
import com.software3.paws_hub_android.DiscoverFragment
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.ActivityMainBinding
import com.software3.paws_hub_android.viewmodel.UserViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        initUI()
        setSupportActionBar(binding.activityMainToolbar)
        userViewModel.isGuestUser.observe(this) { isGuestUser ->
            isGuestUser?.let {
                if (it) navigateToWelcomeActivity()
            }
        }

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

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun setStatusBarColor(color: Int) {
        this.window.statusBarColor = color
        this.window.navigationBarColor = color
    }

    private fun initUI() {
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.activityMainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setStatusBarColor(SurfaceColors.SURFACE_0.getColor(this))
        binding.activityMainNavigation.setOnItemSelectedListener(::onNavigationOptionsItemSelected)
        binding.activityMainToolbar.title = getString(R.string.discover)
    }

    private fun navigateToWelcomeActivity() {
        Intent(this.applicationContext, WelcomeActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun onNavigationOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.activity_main__navigate_profile -> {
            //esto funciona  mal
            supportFragmentManager.commit {
                binding.activityMainToolbar.title = getString(R.string.profile)
                binding.activityMainAppbarLayout.setExpanded(true)
                setReorderingAllowed(true)
                replace(binding.activityMainFragmentView.id, ProfileFragment())
            }
            true
        }
        R.id.activity_main__navigate_create -> {
            false
        }
        R.id.activity_main__navigate_home -> {
            //esto tambien funciona  mal
            supportFragmentManager.commit {
                binding.activityMainToolbar.title = getString(R.string.discover)
                binding.activityMainAppbarLayout.setExpanded(true)
                setReorderingAllowed(true)
                replace(binding.activityMainFragmentView.id, DiscoverFragment())
            }
            true
        }
        else -> false
    }
}