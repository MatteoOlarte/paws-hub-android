package com.software3.paws_hub_android.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.core.AuthState
import com.software3.paws_hub_android.databinding.ActivitySignUpBinding
import com.software3.paws_hub_android.viewmodel.EmailSignUpViewModel


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val authViewModel: EmailSignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(this.layoutInflater)
        initUI()

        authViewModel.authState.observe(this) {
            when (it) {
                AuthState.FAILED -> onAuthFailed()
                AuthState.SUCCESS -> onAuthSuccess()
                AuthState.PENDING -> onAuthPending()
                else -> onAuthFailed()
            }
        }
        binding.registerButton.setOnClickListener {
            initViewModel()
            authViewModel.registerUser()
        }
    }

    private fun initUI() {
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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