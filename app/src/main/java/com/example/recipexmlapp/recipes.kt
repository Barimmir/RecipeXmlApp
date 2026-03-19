package com.example.recipexmlapp
import Category

object STUB {
    private val categories = listOf(
        Category(
            id = 0,
            title = "Бургеры",
            description = "Рецепты всех популярных видов бургеров",
            url = "burger.png"
        ),
        Category(
            id = 1,
            title = "Десерты",
            description = "Самые вкусные рецепты десертов специально для вас",
            url = "dessert.png"
        ),
        Category(
            id = 2,
            title = "Пицца",
            description = "Пицца на любой вкус и цвет. Лучшая подборка для тебя",
            url = "pizza.png"
        ),
        Category(
            id = 3,
            title = "Рыба",
            description = "Печеная, жареная, сушеная, любая рыба на твой вкус",
            url = "fish.png"
        ),
        Category(
            id = 4,
            title = "Супы",
            description = "От классики до экзотики: мир в одной тарелке",
            url = "soup.png"
        ),
        Category(
            id = 5,
            title = "Салаты",
            description = "Хрустящий калейдоскоп под соусом вдохновения",
            url = "salad.png"
        )
    )

    fun getCategories(): List<Category> {
        return categories
    }
}
