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
import com.example.recipexmlapp.data.Recipe
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Callable
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val threadPool = Executors.newFixedThreadPool(10)
    private val json = Json { ignoreUnknownKeys = true }
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

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

        threadPool.submit {
            try {
                println("Выполняю запрос на потоке: ${Thread.currentThread().name}")
                val request = Request.Builder()
                    .url("https://recipes.androidsprint.ru/api/category")
                    .build()
                
                val response = okHttpClient.newCall(request).execute()
                Log.i("!!!", "responseCode: ${response.code}")
                Log.i("!!!", "responseMessage: ${response.message}")
                Log.i("!!!", "Body: ${response.body}")
                
                val responseBody = response.body?.string() ?: ""
                val categoryIds = json.decodeFromString<List<Int>>(responseBody)
                response.close()


                val recipesToId = categoryIds.map { categoryId ->
                    threadPool.submit(Callable {
                        try {
                            println("Выполняю запрос на потоке: ${Thread.currentThread().name}")
                            val request = Request.Builder()
                                .url("https://recipes.androidsprint.ru/api/category/$categoryId/recipes")
                                .build()

                            val recipeResponse = okHttpClient.newCall(request).execute()
                            val recipeResponseBody = recipeResponse.body?.string() ?: ""
                            val recipes = json.decodeFromString<List<Recipe>>(recipeResponseBody)
                            recipeResponse.close()

                        println("Категория $categoryId: получено ${recipes.size} рецептов")
                        recipes.forEach { recipe ->
                            println("  - Рецепт: ${recipe.id} - ${recipe.title}")
                        }

                        categoryId to recipes
                    } catch (e: Exception) {
                        println("Ошибка при получении рецептов для категории $categoryId: ${e.message}")
                        categoryId to emptyList<Recipe>()
                    }
                })
            }
            
            recipesToId.forEach { future ->
                try {
                    val (categoryId, recipes) = future.get()
                    println("Категория $categoryId: ${recipes.size} рецептов загружено")
                    recipes.forEach { recipe ->
                        println("  - ${recipe.title}")
                    }
                } catch (e: Exception) {
                    println("Ошибка при получении результата: ${e.message}")
                }
            }
            } catch (e: Exception) {
                println("Ошибка в основном запросе: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}