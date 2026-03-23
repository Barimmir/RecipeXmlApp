package com.example.recipexmlapp
import com.example.recipexmlapp.model.Recipe
import com.example.recipexmlapp.model.Ingredient

object STUB {
    private val burgerRecipes = listOf(
        Recipe(
            id = 1,
            title = "Классический бургер",
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
            imageUrl = "burger1.jpg"
        ),
        Recipe(
            id = 2,
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
            imageUrl = "burger2.jpg"
        ),
        Recipe(
            id = 3,
            title = "Бургер с беконом",
            ingredients = listOf(
                Ingredient("200", "г", "говяжий фарш"),
                Ingredient("2", "шт", "полоски бекона"),
                Ingredient("1", "шт", "булочка"),
                Ingredient("1", "шт", "лук"),
                Ingredient("20", "г", "сыра")
            ),
            method = listOf(
                "Обжарить бекон до хруста",
                "Приготовить котлету",
                "Собрать бургер с беконом и сыром"
            ),
            imageUrl = "burger3.jpg"
        ),
        Recipe(
            id = 4,
            title = "Вегетарианский бургер",
            ingredients = listOf(
                Ingredient("150", "г", "овощной котлеты"),
                Ingredient("1", "шт", "булочка"),
                Ingredient("2", "шт", "листья салата"),
                Ingredient("1", "шт", "помидор"),
                Ingredient("30", "г", "авокадо")
            ),
            method = listOf(
                "Разогреть овощную котлету",
                "Нарезать овощи",
                "Собрать вегетарианский бургер"
            ),
            imageUrl = "burger4.jpg"
        ),
        Recipe(
            id = 5,
            title = "Двойной бургер",
            ingredients = listOf(
                Ingredient("300", "г", "говяжий фарш"),
                Ingredient("1", "шт", "булочка"),
                Ingredient("2", "шт", "листья салата"),
                Ingredient("2", "шт", "ломтика сыра"),
                Ingredient("2", "шт", "ломтика помидора")
            ),
            method = listOf(
                "Сформировать две котлеты",
                "Обжарить котлеты",
                "Собрать бургер с двумя котлетами"
            ),
            imageUrl = "burger5.jpg"
        ),
        Recipe(
            id = 6,
            title = "Бургер с грибами",
            ingredients = listOf(
                Ingredient("200", "г", "говяжий фарш"),
                Ingredient("100", "г", "шампиньонов"),
                Ingredient("1", "шт", "булочка"),
                Ingredient("20", "г", "сыра"),
                Ingredient("1", "зубчик", "чеснока")
            ),
            method = listOf(
                "Обжарить грибы с чесноком",
                "Приготовить котлету",
                "Собрать бургер с грибами и сыром"
            ),
            imageUrl = "burger6.jpg"
        ),
        Recipe(
            id = 7,
            title = "Острый бургер",
            ingredients = listOf(
                Ingredient("200", "г", "говяжий фарш"),
                Ingredient("1", "шт", "булочка"),
                Ingredient("1", "шт", "острый перец"),
                Ingredient("2", "шт", "листья салата"),
                Ingredient("20", "г", "сыра")
            ),
            method = listOf(
                "Приготовить котлету с острым перцем",
                "Собрать бургер",
                "Добавить острый соус по вкусу"
            ),
            imageUrl = "burger7.jpg"
        ),
        Recipe(
            id = 8,
            title = "Цыпленок бургер",
            ingredients = listOf(
                Ingredient("200", "г", "куриное филе"),
                Ingredient("1", "шт", "булочка"),
                Ingredient("2", "шт", "листья салата"),
                Ingredient("1", "шт", "помидор"),
                Ingredient("30", "г", "майонеза")
            ),
            method = listOf(
                "Обжарить куриное филе",
                "Нарезать овощи",
                "Собрать куриный бургер"
            ),
            imageUrl = "burger8.jpg"
        )
    )
    
    fun getRecipesByCategoryId(categoryId: Int): List<Recipe> {
        return if (categoryId == 0) {
            burgerRecipes
        } else {
            emptyList()
        }
    }
}
