package com.example.recipexmlapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipexmlapp.R
import com.example.recipexmlapp.databinding.FragmentFavoritesBinding
import com.example.recipexmlapp.ui.recipes.recipelist.RecipesAdapter
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {
    
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: FavoritesViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    }
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
            // TODO: Show loading indicator
        }
        
        state.error?.let {
            // TODO: Show error message
        }
        
        if (state.isEmpty) {
            binding.rvFavorites.visibility = View.GONE
            binding.tvEmptyState.visibility = View.VISIBLE
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
