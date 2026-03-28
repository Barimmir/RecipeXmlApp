package com.example.recipexmlapp.ui.recipes.recipe

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.SeekBar
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipexmlapp.R
import com.example.recipexmlapp.adapter.IngredientsAdapter
import com.example.recipexmlapp.adapter.MethodAdapter
import com.example.recipexmlapp.data.Recipe
import android.os.Handler
import android.os.Looper
import com.google.android.material.divider.MaterialDividerItemDecoration
import androidx.core.os.BundleCompat

class RecipeDetailFragment : Fragment() {

    companion object {
        private const val PREFS_NAME = "recipe_favorites"
        private const val FAVORITES_KEY = "favorites_set"

        fun newInstance(recipe: Recipe): RecipeDetailFragment {
            return RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("recipe", recipe)
                }
            }
        }
    }

    private lateinit var recipe: Recipe
    private lateinit var ivRecipeImage: ImageView
    private lateinit var tvRecipeTitle: TextView
    private lateinit var tvPortions: TextView
    private lateinit var seekBarPortions: SeekBar
    private lateinit var ibFavorite: ImageButton
    private lateinit var rvIngredients: RecyclerView
    private lateinit var rvMethod: RecyclerView
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var updateRunnable: Runnable? = null
    private val sharedPrefs by lazy { requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recipe = BundleCompat.getParcelable(it, "recipe", Recipe::class.java)
                ?: throw IllegalArgumentException("Recipe argument is required")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        initRecycler()
        updateFavoriteIcon()
    }

    private fun initUI(view: View) {
        ivRecipeImage = view.findViewById(R.id.ivRecipeImage)
        tvRecipeTitle = view.findViewById(R.id.tvRecipeTitle)
        tvPortions = view.findViewById(R.id.tvPortions)
        seekBarPortions = view.findViewById(R.id.seekBarPortions)
        ibFavorite = view.findViewById(R.id.ibFavorite)

        tvRecipeTitle.text = recipe.title.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }

        try {
            val inputStream = requireContext().assets.open(recipe.imageUrl)
            val drawable = Drawable.createFromStream(inputStream, null)
            ivRecipeImage.setImageDrawable(drawable)
        } catch (_: Exception) {
            ivRecipeImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        ibFavorite.setOnClickListener {
            toggleFavorite()
        }
    }

    private fun initRecycler() {
        rvIngredients = requireView().findViewById(R.id.rvIngredients)
        rvMethod = requireView().findViewById(R.id.rvMethod)

        ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
        rvIngredients.adapter = ingredientsAdapter
        rvIngredients.layoutManager = LinearLayoutManager(requireContext())

        methodAdapter = MethodAdapter(recipe.method)
        rvMethod.adapter = methodAdapter
        rvMethod.layoutManager = LinearLayoutManager(requireContext())

        val ingredientsDivider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        rvIngredients.addItemDecoration(ingredientsDivider)

        val methodDivider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        rvMethod.addItemDecoration(methodDivider)

        seekBarPortions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val portions = progress + 1
                tvPortions.text = portions.toString()

                updateRunnable?.let { handler.removeCallbacks(it) }

                updateRunnable = Runnable {
                    ingredientsAdapter.updateIngredients(portions)
                }

                handler.postDelayed(updateRunnable!!, 300)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                updateRunnable?.let { handler.removeCallbacks(it) }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun saveFavorites(favorites: Set<String>) {
        sharedPrefs.edit()
            .putStringSet(FAVORITES_KEY, favorites)
            .apply()
    }

    private fun getFavorites(): MutableSet<String> {
        val savedFavorites: Set<String>? = sharedPrefs.getStringSet(FAVORITES_KEY, emptySet())
        return HashSet(savedFavorites ?: emptySet())
    }

    private fun updateFavoriteIcon() {
        val favorites = getFavorites()
        if (favorites.contains(recipe.id.toString())) {
            ibFavorite.setImageResource(R.drawable.ic_heart)
        } else {
            ibFavorite.setImageResource(R.drawable.ic_heart_empty)
        }
    }

    private fun toggleFavorite() {
        val favorites = getFavorites()
        val recipeIdString = recipe.id.toString()
        if (favorites.contains(recipeIdString)) {
            favorites.remove(recipeIdString)
            ibFavorite.setImageResource(R.drawable.ic_heart_empty)
        } else {
            favorites.add(recipeIdString)
            ibFavorite.setImageResource(R.drawable.ic_heart)
        }
        saveFavorites(favorites)
    }
}
