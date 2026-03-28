package com.example.recipexmlapp.ui

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recipexmlapp.R
import com.example.recipexmlapp.ui.categories.CategoriesListFragment
import com.example.recipexmlapp.ui.recipes.favorites.FavoritesFragment

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
        
        val btnRecipes = findViewById<Button>(R.id.btn_recipes)
        val btnFavorites = findViewById<Button>(R.id.btn_favorites)
        
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
        
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, CategoriesListFragment())
            .commit()
    }
}