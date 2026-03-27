package com.example.recipexmlapp.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipexmlapp.STUB
import com.example.recipexmlapp.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {
    
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var recipesAdapter: RecipesAdapter
    
    companion object {
        private const val PREFS_NAME = "recipe_favorites"
        private const val FAVORITES_KEY = "favorites_set"
    }
    
    private val sharedPrefs by lazy { requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        loadFavoriteRecipes()
    }
    
    private fun getFavorites(): Set<String> {
        val savedFavorites: Set<String>? = sharedPrefs.getStringSet(FAVORITES_KEY, emptySet())
        return savedFavorites ?: emptySet()
    }
    
    private fun initRecyclerView() {
        recipesAdapter = RecipesAdapter(emptyList()) { recipeId ->
            openRecipeByRecipeId(recipeId)
        }
        
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipesAdapter
        }
    }
    
    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        recipe?.let {
            val recipeDetailFragment = RecipeDetailFragment.newInstance(it)
            
            parentFragmentManager.beginTransaction()
                .replace(com.example.recipexmlapp.R.id.mainContainer, recipeDetailFragment)
                .commit()
        }
    }
    
    private fun loadFavoriteRecipes() {
        val favoriteIds = getFavorites()
        val favoriteIdsInt = favoriteIds.mapNotNull { it.toIntOrNull() }.toSet()
        val favoriteRecipes = STUB.getRecipesByIds(favoriteIdsInt)
        recipesAdapter.updateRecipes(favoriteRecipes)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
