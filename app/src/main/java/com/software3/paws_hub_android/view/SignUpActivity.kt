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
import com.software3.paws_hub_android.databinding.ActivitySignUpBinding
import com.software3.paws_hub_android.viewmodel.EmailAuthViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val authViewModel: EmailAuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(this.layoutInflater)
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
        binding.registerButton.setOnClickListener {
            initViewModel()
            authViewModel.register()
        }
    }

    private fun initUI() {
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.activitySignUpLayoutMain) { v, insets ->
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
        authViewModel.fName = binding.fistNameInput.text.toString()
        authViewModel.lName = binding.lastNameInput.text.toString()
        authViewModel.uName = binding.userNameInput.text.toString()
        authViewModel.email = binding.userEmailInput.text.toString().lowercase()
        authViewModel.password1 = binding.userPasswordInput.text.toString()
        authViewModel.password2 = binding.userPasswordConfirmInput.text.toString()
    }

    private fun onAuthSuccess() {
        binding.toolbarProgressIndicator.visibility = View.INVISIBLE
        Intent(this.applicationContext, MainActivity::class.java). also {
            startActivity(it)
        }
    }

    private fun onAuthPending() {
        binding.toolbarProgressIndicator.visibility = View.VISIBLE
        binding.registerButton.isEnabled = false
    }

    private fun onAuthFailed() {
        Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
        binding.toolbarProgressIndicator.visibility = View.INVISIBLE
        binding.registerButton.isEnabled = true
    }
}