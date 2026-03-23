package com.example.recipexmlapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.recipexmlapp.model.Recipe
import android.os.Build

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
            textView.text = "Recipe: ${it.title}"
        } ?: run {
            textView.text = "Recipe not found"
        }
    }
    
    private fun getRecipeFromArguments(): Recipe? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable("ARG_RECIPE", Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("ARG_RECIPE")
        }
    }
}
