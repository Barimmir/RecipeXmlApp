package com.example.recipexmlapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.recipexmlapp.data.Recipe
import com.example.recipexmlapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val textView = TextView(context).apply {
            textSize = 20f
            setPadding(50, 50, 50, 50)
        }
        return textView
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val recipe = getRecipeFromArguments()
        val textView = view as TextView
        
        recipe?.let {
            textView.text = getString(R.string.recipe_title_format, it.title)
        } ?: run {
            textView.text = getString(R.string.recipe_not_found)
        }
    }
    
    private fun getRecipeFromArguments(): Recipe? {
        return null
    }
}
