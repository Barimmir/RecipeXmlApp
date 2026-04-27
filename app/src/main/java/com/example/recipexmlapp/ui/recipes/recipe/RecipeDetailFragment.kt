package com.example.recipexmlapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipexmlapp.R
import com.example.recipexmlapp.adapter.IngredientsAdapter
import com.example.recipexmlapp.adapter.MethodAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.recipexmlapp.RecipeApplication
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) : OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser) {
            onChangeIngredients(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}

class RecipeDetailFragment : Fragment() {

    private val args: RecipeDetailFragmentArgs by navArgs()


    private val viewModel: RecipeDetailViewModel by viewModels {
        val app = requireActivity().application as RecipeApplication
        app.appContainer.recipeViewModelFactory
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeId = args.recipeId
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
        loadRecipe(args.recipeId)
        initUI(view)
    }

    private fun loadRecipe(recipeId: Int) {
        viewModel.loadRecipe(recipeId)
    }

    private fun initUI(view: View) {
        ivRecipeImage = view.findViewById(R.id.ivRecipeImage)
        tvRecipeTitle = view.findViewById(R.id.tvRecipeTitle)
        tvPortions = view.findViewById(R.id.tvPortions)
        seekBarPortions = view.findViewById(R.id.seekBarPortions)
        ibFavorite = view.findViewById(R.id.ibFavorite)
        rvIngredients = view.findViewById(R.id.rvIngredients)
        rvMethod = view.findViewById(R.id.rvMethod)

        ingredientsAdapter = IngredientsAdapter()
        rvIngredients.adapter = ingredientsAdapter
        rvIngredients.layoutManager = LinearLayoutManager(requireContext())

        methodAdapter = MethodAdapter()
        rvMethod.adapter = methodAdapter
        rvMethod.layoutManager = LinearLayoutManager(requireContext())

        val ingredientsDivider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        rvIngredients.addItemDecoration(ingredientsDivider)

        val methodDivider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        rvMethod.addItemDecoration(methodDivider)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                state.recipe?.let { recipe ->
                    tvRecipeTitle.text = recipe.title


                    state.recipeImageUrl?.let { imageUrl ->
                        Glide.with(requireContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.img_placeholder)
                            .error(R.drawable.img_error)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(ivRecipeImage)
                    }

                    ingredientsAdapter.updateIngredients(recipe.ingredients)
                    ingredientsAdapter.updatePortions(state.portionsCount)
                    methodAdapter.updateMethod(recipe.method)

                    tvPortions.text = state.portionsCount.toString()
                    seekBarPortions.progress = state.portionsCount - 1

                    if (state.isFavorite) {
                        ibFavorite.setImageResource(R.drawable.ic_heart)
                    } else {
                        ibFavorite.setImageResource(R.drawable.ic_heart_empty)
                    }
                }
            }
        }

        ibFavorite.setOnClickListener {
            viewModel.onFavoritesClicked()
        }

        seekBarPortions.setOnSeekBarChangeListener(PortionSeekBarListener { progress ->
            val portions = progress + 1
            viewModel.updatePortions(portions)
        })
    }


}
