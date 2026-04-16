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
        println("CategoriesListFragment: onViewCreated called")
        
        setupRecyclerView()
        observeViewModel()
        
        println("CategoriesListFragment: Calling viewModel.initialize()")
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
        println("CategoriesListFragment: updateUI called with isLoading=${state.isLoading}, categories=${state.categories.size}, error=${state.error}")
        
        if (state.isLoading) {
            println("CategoriesListFragment: Showing loading state")
            // TODO: Show loading indicator
        }
        
        state.error?.let {
            println("CategoriesListFragment: Showing error: $it")
            // TODO: Show error message
        }
        
        println("CategoriesListFragment: Updating adapter with ${state.categories.size} categories")
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
        println("CategoriesListFragment: openRecipesByCategoryId called with categoryId=$categoryId")
        val categories = viewModel.state.value.categories
        println("CategoriesListFragment: Available categories: ${categories.size}")
        val category = categories.find { it.id == categoryId }
            ?: throw IllegalArgumentException("Category with id $categoryId not found")
        
        println("CategoriesListFragment: Found category: ${category.id} - ${category.title}")
        val action = CategoriesListFragmentDirections
            .actionCategoriesListFragmentToRecipesListFragment(
                categoryId = category.id,
                categoryName = category.title,
                categoryImageUrl = category.imageName
            )
        
        println("CategoriesListFragment: Navigating to RecipesListFragment")
        findNavController().navigate(action)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
