package com.example.recipexmlapp.ui.recipes.recipelist

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipexmlapp.R
import com.example.recipexmlapp.databinding.FragmentRecipesListBinding
import kotlinx.coroutines.launch

class RecipesListFragment : Fragment() {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipesListViewModel by viewModels()
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

        setupRecyclerView()
        observeViewModel()
        
        val categoryId = arguments?.getInt(ARG_CATEGORY_ID)
        val categoryName = arguments?.getString(ARG_CATEGORY_NAME)
        val categoryImageUrl = arguments?.getString(ARG_CATEGORY_IMAGE_URL)
        
        viewModel.initialize(categoryId, categoryName, categoryImageUrl)
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                updateUI(state)
            }
        }
    }

    private fun updateUI(state: RecipesListState) {
        setupHeader(state.categoryName, state.categoryImageUrl)
        
        if (state.isLoading) {
            // TODO: Show loading indicator
        }
        
        state.error?.let {
            // TODO: Show error message
        }
        
        recipesAdapter.updateRecipes(state.recipes)
    }

    private fun setupHeader(categoryName: String?, categoryImageUrl: String?) {
        binding.tvCategoryName.text = categoryName ?: "Category"

        categoryImageUrl?.let { imageUrl ->
            try {
                val inputStream = requireContext().assets.open(imageUrl)
                val drawable = Drawable.createFromStream(inputStream, null)
                binding.ivCategoryImage.setImageDrawable(drawable)
            } catch (_: Exception) {
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

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = Bundle().apply {
            putInt("recipeId", recipeId)
        }
        
        findNavController().navigate(R.id.recipeDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
