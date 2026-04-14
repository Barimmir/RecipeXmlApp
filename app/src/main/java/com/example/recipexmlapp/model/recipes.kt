package com.example.recipexmlapp.model

import com.example.recipexmlapp.data.Recipe
import com.example.recipexmlapp.data.Ingredient

object STUB {
    private val burgerRecipes = listOf(
        Recipe(
            id = 1,
            title = "Чизбургер",
            ingredients = listOf(
                Ingredient("150", "г", "говяжий фарш"),
                Ingredient("1", "шт", "булочка"),
                Ingredient("1", "шт", "лук"),
                Ingredient("2", "шт", "соленых огурца"),
                Ingredient("30", "г", "сыра")
            ),
            method = listOf(
                "Приготовить котлету",
                "Положить сыр на котлету",
                "Собрать бургер с соусом и огурцами"
            ),
            imageUrl = "burger_cheeseburger.png"
        ),
        Recipe(
            id = 2,
            title = "Хамбургер",
            ingredients = listOf(
                Ingredient("200", "г", "говяжий фарш"),
                Ingredient("1", "шт", "булочка"),
                Ingredient("2", "шт", "листья салата"),
                Ingredient("2", "шт", "ломтика помидора"),
                Ingredient("20", "г", "сыра чеддер")
            ),
            method = listOf(
                "Сформировать котлету из фарша",
                "Обжарить котлету на гриле 4 минуты с каждой стороны",
                "Собрать бургер: булочка, салат, помидор, котлета, сыр, булочка"
            ),
            imageUrl = "burger_hamburger.png"
        )
    )

    fun getRecipesByCategoryId(categoryId: Int): List<Recipe> {
        return if (categoryId == 0) {
            burgerRecipes
        } else {
            emptyList()
        }
    }

    fun getRecipeById(id: Int): Recipe? {
        return burgerRecipes.find { it.id == id }
    }

    fun getRecipesByIds(ids: Set<Int>): List<Recipe> {
        return burgerRecipes.filter { it.id in ids }
    }
}
