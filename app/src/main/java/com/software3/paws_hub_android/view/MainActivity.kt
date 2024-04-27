package com.software3.paws_hub_android.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
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
        userViewModel.isGuestUser.observe(this) { isGuestUser ->
            isGuestUser?.let {
                if (it) navigateToWelcomeActivity()
            }
        }
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

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initUI() {
        setSupportActionBar(binding.activityMainToolbar)
        setContentView(binding.root)
        binding.activityMainNavigation.setOnItemSelectedListener(::onNavigationOptionsItemSelected)
        binding.activityMainToolbar.title = getString(R.string.discover)
    }

    private fun navigateToWelcomeActivity() =
        Intent(this.applicationContext, WelcomeActivity::class.java).also {
            startActivity(it)
        }


    //Cambiar esto segun las fucking issues
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