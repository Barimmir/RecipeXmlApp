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
import com.example.recipexmlapp.data.Recipe
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val threadPool = Executors.newFixedThreadPool(10)

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
            Log.i("!!!", "responseCode: ${connection.responseCode}")
            Log.i("!!!", "responseMessage: ${connection.responseMessage}")
            val responseBody = connection.getInputStream().bufferedReader().readText()
            Log.i("!!!", "Body: $responseBody")
            val json = Json { ignoreUnknownKeys = true }
            val categories = json.decodeFromString<List<Category>>(responseBody)
            connection.disconnect()

            val categoryIds = categories.map { it.id }

            val recipesToId = categoryIds.map { categoryId ->
                threadPool.submit(Callable {
                    try {
                        println("Getting recipes for category $categoryId on thread: ${Thread.currentThread().name}")
                        val recipeUrl =
                            URL("https://recipes.androidsprint.ru/api/category/$categoryId/recipes")
                        val recipeConnection = recipeUrl.openConnection() as HttpURLConnection
                        recipeConnection.requestMethod = "GET"

                        val recipeResponseBody =
                            recipeConnection.getInputStream().bufferedReader().readText()
                        val recipes = json.decodeFromString<List<Recipe>>(recipeResponseBody)
                        recipeConnection.disconnect()

                        println("Category $categoryId: received ${recipes.size} recipes")
                        recipes.forEach { recipe ->
                            println("  - Recipe: ${recipe.id} - ${recipe.title}")
                        }

                        categoryId to recipes
                    } catch (e: Exception) {
                        println("Error getting recipes for category $categoryId: ${e.message}")
                        categoryId to emptyList<Recipe>()
                    }
                })
            }
            recipesToId.forEach { recipesToId ->
                try {
                    val (categoryId, recipes) = recipesToId.get()
                    println("Категория $categoryId: ${recipes.size} рецептов загружено")
                    recipes.forEach { recipe ->
                        println("  - ${recipe.title}")
                    }
                } catch (e: Exception) {
                    println("Ошибка при получении результата: ${e.message}")
                }
            }
        }
        thread.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}