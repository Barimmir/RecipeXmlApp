package com.example.recipexmlapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Find navigation buttons
        val btnRecipes = findViewById<android.widget.Button>(R.id.btn_recipes)
        val btnFavorites = findViewById<android.widget.Button>(R.id.btn_favorites)
        
        // Set click listeners
        btnRecipes.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, CategoriesListFragment())
                .commit()
        }
        
        btnFavorites.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, FavoritesFragment())
                .commit()
        }
        
        // Launch CategoriesListFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, CategoriesListFragment())
            .commit()
    }
}