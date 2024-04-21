package com.software3.paws_hub_android.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.elevation.SurfaceColors
import com.software3.paws_hub_android.databinding.ActivityEditProfileBinding


class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(this.layoutInflater)
        enableEdgeToEdge()
        initUI()
    }

    private fun initUI() {
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        paintStatusBar()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun paintStatusBar() {
        val color = SurfaceColors.SURFACE_0.getColor(this)
        this.window.statusBarColor = color
        this.window.navigationBarColor = color
    }
}