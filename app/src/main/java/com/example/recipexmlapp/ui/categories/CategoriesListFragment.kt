package com.example.recipexmlapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipexmlapp.R
import com.example.recipexmlapp.databinding.FragmentListCategoriesBinding
import com.example.recipexmlapp.ui.recipes.recipelist.RecipesListFragment
import kotlinx.coroutines.launch

class CategoriesListFragment : Fragment() {
    
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: CategoriesListViewModel by viewModels()
    private lateinit var categoriesAdapter: CategoriesListAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
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
    
    private fun updateUI(state: CategoriesListState) {
        if (state.isLoading) {
            // TODO: Show loading indicator
        }
        
        state.error?.let {
            // TODO: Show error message
        }
        
        categoriesAdapter.updateCategories(state.categories)
    }
    
    private fun setupRecyclerView() {
        categoriesAdapter = CategoriesListAdapter(emptyList())
        
        categoriesAdapter.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
        
        val recyclerView: RecyclerView = binding.root.findViewById(R.id.rvCategories)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = categoriesAdapter
    }
    
    private fun openRecipesByCategoryId(categoryId: Int) {
        val categories = viewModel.state.value.categories
        val category = categories.find { it.id == categoryId }
        
        val categoryName = category?.title ?: ""
        val categoryImageUrl = category?.imageName ?: ""
        
        val bundle = Bundle().apply {
            putInt(RecipesListFragment.ARG_CATEGORY_ID, categoryId)
            putString(RecipesListFragment.ARG_CATEGORY_NAME, categoryName)
            putString(RecipesListFragment.ARG_CATEGORY_IMAGE_URL, categoryImageUrl)
        }
        
        findNavController().navigate(R.id.recipesListFragment, bundle)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
