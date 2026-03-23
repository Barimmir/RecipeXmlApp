package com.example.recipexmlapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipexmlapp.STUB
import com.example.recipexmlapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment() {
    
    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!
    
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null
    
    private lateinit var recipesAdapter: RecipesAdapter
    
    companion object {
        const val ARG_CATEGORY_ID = "ARG_CATEGORY_ID"
        const val ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME"
        const val ARG_CATEGORY_IMAGE_URL = "ARG_CATEGORY_IMAGE_URL"
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        categoryId = arguments?.getInt(ARG_CATEGORY_ID)
        categoryName = arguments?.getString(ARG_CATEGORY_NAME)
        categoryImageUrl = arguments?.getString(ARG_CATEGORY_IMAGE_URL)
        
        setupHeader()
        setupRecyclerView()
        loadRecipes()
    }
    
    private fun setupHeader() {
        binding.tvCategoryName.text = categoryName ?: "Category"
        
        // Load category image from assets
        categoryImageUrl?.let { imageUrl ->
            try {
                val inputStream = requireContext().assets.open(imageUrl)
                val drawable = android.graphics.drawable.Drawable.createFromStream(inputStream, null)
                binding.ivCategoryImage.setImageDrawable(drawable)
            } catch (e: Exception) {
                // Set placeholder if image not found
                binding.ivCategoryImage.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }
    }
    
    private fun setupRecyclerView() {
        recipesAdapter = RecipesAdapter(emptyList()) { recipeId ->
            openRecipeByRecipeId(recipeId)
        }
        
        binding.rvRecipes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipesAdapter
        }
    }
    
    private fun loadRecipes() {
        categoryId?.let { id ->
            val recipes = STUB.getRecipesByCategoryId(id)
            recipesAdapter.updateRecipes(recipes)
        }
    }
    
    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)
        recipe?.let {
            val bundle = Bundle()
            bundle.putParcelable("ARG_RECIPE", it)
            
            val recipeFragment = RecipeFragment()
            recipeFragment.arguments = bundle
            
            parentFragmentManager.beginTransaction()
                .replace(com.example.recipexmlapp.R.id.mainContainer, recipeFragment)
                .commit()
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
