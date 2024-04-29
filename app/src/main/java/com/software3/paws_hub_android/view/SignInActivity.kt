package com.software3.paws_hub_android.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.core.enums.AuthProvider
import com.software3.paws_hub_android.core.enums.TransactionState
import com.software3.paws_hub_android.databinding.ActivitySignInBinding
import com.software3.paws_hub_android.model.UserSignIn
import com.software3.paws_hub_android.view.main_activity.MainActivity
import com.software3.paws_hub_android.viewmodel.SignInViewModel


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val authViewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(this.layoutInflater)
        initUI()


        authViewModel.authState.observe(this) {
            when (it) {
                TransactionState.FAILED -> onAuthFailed()
                TransactionState.SUCCESS -> onAuthSuccess()
                TransactionState.PENDING -> onAuthPending()
                else -> onAuthFailed()
            }
        }
        binding.loginButton.setOnClickListener {
            val userSignIn: UserSignIn = getUserFromActivity()
            val password: String = binding.userPasswordInput.text.toString()
            authViewModel.loginIn(userSignIn, password, AuthProvider.EMAIL_PASSWORD)
        }
    }

    private fun initUI() {
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getUserFromActivity() = UserSignIn(
        email = binding.userEmailInput.text.toString().lowercase(),
        phone = null
    )

    private fun onAuthSuccess() {
        binding.toolbarProgressIndicator.visibility = View.GONE
        binding.loginButton.isEnabled = true
        Intent(this.applicationContext, MainActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun onAuthPending() {
        binding.toolbarProgressIndicator.visibility = View.VISIBLE
        binding.loginButton.isEnabled = false
    }

    private fun onAuthFailed() = MaterialAlertDialogBuilder(this).setTitle("Error")
        .setMessage(getErrorMessage(authViewModel.errorstr))
        .setPositiveButton("Error") { dialog, _ ->
            binding.toolbarProgressIndicator.visibility = View.GONE
            binding.loginButton.isEnabled = true
            dialog.dismiss()
        }
        .show()

    private fun getErrorMessage(error: String) = when (error) {
        "error_fields_required" -> getString(R.string.error_fields_required)
        else -> error
    }
}