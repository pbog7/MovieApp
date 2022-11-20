package com.example.common.base

import androidx.appcompat.app.AppCompatActivity

abstract class BaseNavigationActivity: AppCompatActivity() {

    abstract fun showBottomNavigation()
    abstract fun hideBottomNavigation()
}