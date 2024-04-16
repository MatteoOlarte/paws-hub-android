package com.software3.paws_hub_android.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.elevation.SurfaceColors
import com.software3.paws_hub_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    /*
    * MVVM
    * */
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        initUI()
        setSupportActionBar(binding.activityMainToolbar)
        setStatusBarColor(SurfaceColors.SURFACE_0.getColor(this))
    }

    private fun setStatusBarColor(color: Int) {
        window.statusBarColor = color
        window.navigationBarColor = color
    }

    private fun initUI() {
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.activityMainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}