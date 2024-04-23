package com.software3.paws_hub_android.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.elevation.SurfaceColors
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.databinding.ActivitySignUpBinding
import com.software3.paws_hub_android.viewmodel.EmailSignUpViewModel


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val authViewModel: EmailSignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(this.layoutInflater)
        initUI()
        setStatusBarColor(SurfaceColors.SURFACE_0.getColor(this))
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        authViewModel.authState.observe(this) {
            when (it) {
                TransactionState.FAILED -> onAuthFailed()
                TransactionState.SUCCESS -> onAuthSuccess()
                TransactionState.PENDING -> onAuthPending()
                else -> onAuthFailed()
            }
        }
        binding.registerButton.setOnClickListener {
            initViewModel()
            authViewModel.registerUser()
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
        binding.toolbarProgressIndicator.visibility = View.GONE
        binding.registerButton.isEnabled = true
        Intent(this.applicationContext, MainActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun onAuthPending() {
        binding.toolbarProgressIndicator.visibility = View.VISIBLE
        binding.registerButton.isEnabled = false
    }

    private fun onAuthFailed() = MaterialAlertDialogBuilder(this).setTitle("Error")
        .setMessage(getErrorMessage(authViewModel.message))
        .setPositiveButton("Error") { dialog, _ ->
            binding.toolbarProgressIndicator.visibility = View.GONE
            binding.registerButton.isEnabled = true
            dialog.dismiss()
        }
        .show()

    private fun getErrorMessage(error: String) = when (error) {
        "error_fields_required" -> getString(R.string.error_fields_required)
        "error_invalid_email" -> getString(R.string.error_invalid_email)
        "error_password_mismatch" -> getString(R.string.error_password_mismatch)
        else -> error
    }
}