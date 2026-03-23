package com.example.recipexmlapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class RecipeFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Create a simple TextView without layout file
        val textView = TextView(context).apply {
            text = "Recipe Fragment - Recipe Details Screen"
            textSize = 20f
            setPadding(50, 50, 50, 50)
        }
        return textView
    }
}
