package com.example.recipexmlapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.recipexmlapp.R
import com.example.recipexmlapp.data.Category
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        println("Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")
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
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
        }

        btnFavorites.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
        }
        val thread = Thread {
            println("Выполняю запрос на потоке: ${Thread.currentThread().name}")
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.content
            Log.i("!!!", "responseCode: ${connection.responseCode}")
            Log.i("!!!", "responseMessage: ${connection.responseMessage}")
            val responseBody = connection.getInputStream().bufferedReader().readText()
            Log.i("!!!", "Body: $responseBody")
            val json = Json { ignoreUnknownKeys = true }
            val categories = json.decodeFromString<List<Category>>(responseBody)
        }
        thread.start()
    }
}