package com.example.recipexmlapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipexmlapp.R
import com.example.recipexmlapp.adapter.IngredientsAdapter
import com.example.recipexmlapp.adapter.MethodAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeDetailFragment : Fragment() {

    companion object {
        fun newInstance(recipeId: Int): RecipeDetailFragment {
            return RecipeDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("recipeId", recipeId)
                }
            }
        }
    }

    private val viewModel: RecipeDetailViewModel by viewModels {
        RecipeDetailViewModelFactory(requireActivity().application)
    }
    private var recipeId: Int = 0
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
            recipeId = it.getInt("recipeId", 0)
            if (recipeId == 0) {
                throw IllegalArgumentException("RecipeId argument is required")
            }
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
        viewModel.loadRecipe(recipeId)
        initUI(view)
        initRecycler()
    }

    private fun initUI(view: View) {
        ivRecipeImage = view.findViewById(R.id.ivRecipeImage)
        tvRecipeTitle = view.findViewById(R.id.tvRecipeTitle)
        tvPortions = view.findViewById(R.id.tvPortions)
        seekBarPortions = view.findViewById(R.id.seekBarPortions)
        ibFavorite = view.findViewById(R.id.ibFavorite)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            state.recipe?.let { recipe ->
                tvRecipeTitle.text = recipe.title.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                }

                try {
                    val inputStream = requireContext().assets.open(recipe.imageUrl)
                    val drawable = Drawable.createFromStream(inputStream, null)
                    ivRecipeImage.setImageDrawable(drawable)
                } catch (_: Exception) {
                    ivRecipeImage.setImageResource(android.R.drawable.ic_menu_gallery)
                }
                
                ingredientsAdapter.updateIngredients(recipe.ingredients)
                methodAdapter.updateMethod(recipe.method)
            }
            
            tvPortions.text = state.portionsCount.toString()
            seekBarPortions.progress = state.portionsCount - 1
            
            if (state.isFavorite) {
                ibFavorite.setImageResource(R.drawable.ic_heart)
            } else {
                ibFavorite.setImageResource(R.drawable.ic_heart_empty)
            }
        }

        ibFavorite.setOnClickListener {
            viewModel.onFavoritesClicked()
        }
    }

    private fun initRecycler() {
        rvIngredients = requireView().findViewById(R.id.rvIngredients)
        rvMethod = requireView().findViewById(R.id.rvMethod)

        ingredientsAdapter = IngredientsAdapter(emptyList())
        rvIngredients.adapter = ingredientsAdapter
        rvIngredients.layoutManager = LinearLayoutManager(requireContext())

        methodAdapter = MethodAdapter(emptyList())
        rvMethod.adapter = methodAdapter
        rvMethod.layoutManager = LinearLayoutManager(requireContext())

        val ingredientsDivider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        rvIngredients.addItemDecoration(ingredientsDivider)

        val methodDivider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        rvMethod.addItemDecoration(methodDivider)

        seekBarPortions.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val portions = progress + 1
                    viewModel.updatePortions(portions)

                    updateRunnable?.let { handler.removeCallbacks(it) }

                    updateRunnable = Runnable {
                        viewModel.state.value?.recipe?.let { recipe ->
                            ingredientsAdapter.updatePortions(viewModel.state.value?.portionsCount ?: 1)
                        }
                    }

                    handler.postDelayed(updateRunnable!!, 300)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                updateRunnable?.let { handler.removeCallbacks(it) }
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

}
