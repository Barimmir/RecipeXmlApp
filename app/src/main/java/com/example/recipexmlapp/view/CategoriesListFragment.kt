package com.example.recipexmlapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipexmlapp.R
import com.example.recipexmlapp.databinding.FragmentListCategoriesBinding
import com.example.recipexmlapp.model.CategoryRepository

class CategoriesListFragment : Fragment() {
    
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding get() = _binding!!
    
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
        initRecycler()
    }
    
    private fun initRecycler() {
        val categories = CategoryRepository.getCategories()
        val adapter = CategoriesListAdapter(categories)
        
        adapter.setOnItemClickListener(object : CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick() {
                openRecipesByCategoryId()
            }
        })
        
        val recyclerView: RecyclerView = binding.root.findViewById(R.id.rvCategories)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
    }
    
    private fun openRecipesByCategoryId() {
        val recipesFragment = RecipesListFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, recipesFragment)
            .addToBackStack(null)
            .commit()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
