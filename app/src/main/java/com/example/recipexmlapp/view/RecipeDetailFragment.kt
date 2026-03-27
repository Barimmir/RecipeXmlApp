package com.example.recipexmlapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.SeekBar
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipexmlapp.R
import com.example.recipexmlapp.adapter.IngredientsAdapter
import com.example.recipexmlapp.adapter.MethodAdapter
import com.example.recipexmlapp.model.Recipe
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeDetailFragment : Fragment() {

    private lateinit var recipe: Recipe
    private lateinit var ivRecipeImage: ImageView
    private lateinit var tvRecipeTitle: TextView
    private lateinit var tvPortions: TextView
    private lateinit var seekBarPortions: SeekBar
    private lateinit var ibFavorite: ImageButton
    private lateinit var rvIngredients: RecyclerView
    private lateinit var rvMethod: RecyclerView
    private lateinit var ingredientsAdapter: IngredientsAdapter
    private lateinit var methodAdapter: MethodAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var updateRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable("recipe", Recipe::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.getParcelable("recipe")
            } ?: throw IllegalArgumentException("Recipe argument is required")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recipe_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        initRecycler()
    }

    private fun initUI(view: View) {
        ivRecipeImage = view.findViewById(R.id.ivRecipeImage)
        tvRecipeTitle = view.findViewById(R.id.tvRecipeTitle)
        tvPortions = view.findViewById(R.id.tvPortions)
        seekBarPortions = view.findViewById(R.id.seekBarPortions)
        ibFavorite = view.findViewById(R.id.ibFavorite)
        
        tvRecipeTitle.text = recipe.title.replaceFirstChar { 
            if (it.isLowerCase()) it.titlecase() else it.toString() 
        }
        
        try {
            val inputStream = requireContext().assets.open(recipe.imageUrl)
            val drawable = android.graphics.drawable.Drawable.createFromStream(inputStream, null)
            ivRecipeImage.setImageDrawable(drawable)
        } catch (_: Exception) {
            ivRecipeImage.setImageResource(android.R.drawable.ic_menu_gallery)
        }
        
        ibFavorite.setOnClickListener {
            ibFavorite.setImageResource(R.drawable.ic_heart)
        }
    }

    private fun initRecycler() {
        rvIngredients = requireView().findViewById(R.id.rvIngredients)
        rvMethod = requireView().findViewById(R.id.rvMethod)

        ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
        rvIngredients.adapter = ingredientsAdapter
        rvIngredients.layoutManager = LinearLayoutManager(requireContext())

        methodAdapter = MethodAdapter(recipe.method)
        rvMethod.adapter = methodAdapter
        rvMethod.layoutManager = LinearLayoutManager(requireContext())

        val ingredientsDivider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        rvIngredients.addItemDecoration(ingredientsDivider)

        val methodDivider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        rvMethod.addItemDecoration(methodDivider)

        seekBarPortions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val portions = progress + 1
                tvPortions.text = portions.toString()
                
                updateRunnable?.let { handler.removeCallbacks(it) }
                
                updateRunnable = Runnable {
                    ingredientsAdapter.updateIngredients(portions)
                }
                
                handler.postDelayed(updateRunnable!!, 300)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                updateRunnable?.let { handler.removeCallbacks(it) }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    companion object {
        fun newInstance(recipe: Recipe): RecipeDetailFragment {
            return RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("recipe", recipe)
                }
            }
        }
    }
}
