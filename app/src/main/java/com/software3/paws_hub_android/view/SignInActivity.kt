package com.software3.paws_hub_android.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.elevation.SurfaceColors
import com.software3.paws_hub_android.AuthState
import com.software3.paws_hub_android.databinding.ActivitySignInBinding
import com.software3.paws_hub_android.viewmodel.EmailSignInViewModel


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val authViewModel: EmailSignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(this.layoutInflater)
        initUI()
        setStatusBarColor(SurfaceColors.SURFACE_0.getColor(this))
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        authViewModel.authState.observe(this) {
            when (it) {
                AuthState.FAILED -> onAuthFailed()
                AuthState.SUCCESS -> onAuthSuccess()
                AuthState.PENDING -> onAuthPending()
                null -> onAuthFailed()
            }
        }
        binding.loginButton.setOnClickListener {
            initViewModel()
            authViewModel.login()
        }
    }

    private fun initUI() {
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.activitySignInLayoutMain) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setStatusBarColor(color: Int) {
        window.statusBarColor = color
        window.navigationBarColor = color
    }

    private fun initViewModel() {
        authViewModel.email = binding.userEmailInput.text.toString().lowercase()
        authViewModel.password = binding.userPasswordInput.text.toString()
    }

    private fun onAuthSuccess() {
        binding.toolbarProgressIndicator.visibility = View.GONE
        binding.loginButton.isEnabled = true
        Intent(this.applicationContext, MainActivity::class.java). also {
            startActivity(it)
        }
    }

    private fun onAuthPending() {
        binding.toolbarProgressIndicator.visibility = View.VISIBLE
        binding.loginButton.isEnabled = false
    }

    private fun onAuthFailed() {
        binding.toolbarProgressIndicator.visibility = View.GONE
        binding.loginButton.isEnabled = true
        Toast.makeText(this, "ERROR ${authViewModel.message}", Toast.LENGTH_LONG).show()
    }
}