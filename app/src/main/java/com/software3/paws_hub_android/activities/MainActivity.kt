package com.software3.paws_hub_android.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.elevation.SurfaceColors
import com.software3.paws_hub_android.R
import com.software3.paws_hub_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    /*
    * MVVM
    * */
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.initUI()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.activityMainToolbar)
        setStatusBarColor(SurfaceColors.SURFACE_0.getColor(this))
    }

    private fun setStatusBarColor(color: Int) {
        window.statusBarColor = color
        window.navigationBarColor = color
    }

    private fun initUI() {
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main__layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}