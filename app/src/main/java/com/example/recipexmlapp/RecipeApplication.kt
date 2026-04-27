package com.example.recipexmlapp

import android.app.Application
import com.example.recipexmlapp.di.AppContainer

class RecipeApplication : Application() {
    
    lateinit var appContainer: AppContainer
        private set
    
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
