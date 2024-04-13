package com.software3.paws_hub_android

import android.app.Application
import com.google.android.material.color.DynamicColors

class PawsHub : Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}