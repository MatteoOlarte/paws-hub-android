package com.software3.paws_hub_android.ui.view.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.software3.paws_hub_android.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(this.layoutInflater)

        initUI()
        binding.welcomeActivityBtnLogin.setOnClickListener(::btnLoginClick)
        binding.welcomeActivityBtnRegister.setOnClickListener(::btnRegisterClick)
    }

    private fun initUI() {
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.activityWelcomeLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun btnLoginClick(view: View?) {
        Intent(this.applicationContext, SignInActivity::class.java).also {
            startActivity(it)
        }
    }

    private fun btnRegisterClick(view: View?) {
        Intent(this.applicationContext, SignUpActivity::class.java).also {
            startActivity(it)
        }
    }
}