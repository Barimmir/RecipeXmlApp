package com.example.recipexmlapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipexmlapp.databinding.FragmentFavoritesBinding
import com.example.recipexmlapp.ui.recipes.recipelist.RecipesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var recipesAdapter: RecipesAdapter

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
        observeViewModel()

        viewModel.initialize()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                updateUI(state)
            }
        }
    }

    private fun updateUI(state: FavoritesState) {
        if (state.isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvFavorites.visibility = View.GONE
            binding.tvEmptyState.visibility = View.GONE
        }

        state.error?.let { errorMessage ->
            binding.progressBar.visibility = View.GONE
            binding.rvFavorites.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.tvEmptyState.text = errorMessage
            return
        }

        if (!state.isLoading) {
            binding.progressBar.visibility = View.GONE
        }

        if (state.isEmpty) {
            binding.rvFavorites.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
            if (binding.tvEmptyState.text.toString() == "Нет избранных рецептов") {
                binding.tvEmptyState.text = "Нет избранных рецептов"
            }
        } else {
            binding.rvFavorites.visibility = View.VISIBLE
            binding.tvEmptyState.visibility = View.GONE
            recipesAdapter.updateRecipes(state.favoriteRecipes)
        }
    }

    private fun initRecyclerView() {
        recipesAdapter = RecipesAdapter(emptyList()) { recipeId ->
            openRecipeByRecipeId(recipeId)
        }

        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipesAdapter
        }

        binding.ibBackToCategories.setOnClickListener {
            val action = FavoritesFragmentDirections
                .actionFavoritesFragmentToCategoriesListFragment()
            findNavController().navigate(action)
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val action = FavoritesFragmentDirections
            .actionFavoritesFragmentToRecipeDetailFragment(recipeId)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
