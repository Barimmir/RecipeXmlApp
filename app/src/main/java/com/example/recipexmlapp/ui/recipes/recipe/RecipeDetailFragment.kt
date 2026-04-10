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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipexmlapp.R
import com.example.recipexmlapp.adapter.IngredientsAdapter
import com.example.recipexmlapp.adapter.MethodAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration
import android.widget.SeekBar.OnSeekBarChangeListener

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
    }

    private fun initUI(view: View) {
        ivRecipeImage = view.findViewById(R.id.ivRecipeImage)
        tvRecipeTitle = view.findViewById(R.id.tvRecipeTitle)
        tvPortions = view.findViewById(R.id.tvPortions)
        seekBarPortions = view.findViewById(R.id.seekBarPortions)
        ibFavorite = view.findViewById(R.id.ibFavorite)
        rvIngredients = view.findViewById(R.id.rvIngredients)
        rvMethod = view.findViewById(R.id.rvMethod)

        // Инициализация адаптеров
        ingredientsAdapter = IngredientsAdapter()
        rvIngredients.adapter = ingredientsAdapter
        rvIngredients.layoutManager = LinearLayoutManager(requireContext())

        methodAdapter = MethodAdapter()
        rvMethod.adapter = methodAdapter
        rvMethod.layoutManager = LinearLayoutManager(requireContext())

        // Добавление разделителей
        val ingredientsDivider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        rvIngredients.addItemDecoration(ingredientsDivider)

        val methodDivider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        rvMethod.addItemDecoration(methodDivider)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            state.recipe?.let { recipe ->
                tvRecipeTitle.text = recipe.title.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase() else it.toString()
                }

                // Установка изображения из стейта
                ivRecipeImage.setImageDrawable(state.recipeImage)
                
                // Обновление адаптеров данными из стейта
                ingredientsAdapter.dataSet = recipe.ingredients
                ingredientsAdapter.updatePortions(state.portionsCount)
                methodAdapter.dataSet = recipe.method
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

        // Настройка SeekBar для обновления порций
        seekBarPortions.setOnSeekBarChangeListener(PortionSeekBarListener { progress ->
            val portions = progress + 1
            viewModel.updatePortions(portions)
        })
    }


}
