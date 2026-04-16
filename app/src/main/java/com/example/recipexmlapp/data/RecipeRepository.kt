package com.example.recipexmlapp.data


object RecipeRepository {
    fun getRecipes(): List<Recipe> {
        return listOf(
            Recipe(
                id = 0,
                title = "Классический бургер с говядиной",
                description = "C juicy beef burger with fresh vegetables and classic sauces. Perfect choice for lunch or dinner",
                ingredients = listOf(
                    Ingredient("0.5", "кг", "Говяжий фарш"),
                    Ingredient("1.0", "шт", "Луковица, мелко нарезанная"),
                    Ingredient("2.0", "зубч", "Чеснок, измельченный"),
                    Ingredient("4.0", "шт", "Булочки для бургера"),
                    Ingredient("4.0", "шт", "Листа салата"),
                    Ingredient("1.0", "шт", "Помидор, нарезанный кольцами"),
                    Ingredient("2.0", "ст. л.", "Горчица"),
                    Ingredient("2.0", "ст. л.", "Кетчуп"),
                    Ingredient("по вкусу", "", "Соль и черный перец")
                ),
                method = listOf(
                    "1. В глубокой миске смешайте говяжий фарш, лук, чеснок, соль и перец. Разделите фарш на 4 равные части и сформируйте котлеты.",
                    "2. Разогрейте сковороду на среднем огне. Обжаривайте котлеты с каждой стороны в течение 4-5 минут или до желаемой степени прожарки.",
                    "3. В то время как котлеты готовятся, подготовьте булочки. Разрежьте их пополам и обжарьте на сковороде до золотистой корочки.",
                    "4. Смазать нижние половинки булочек горчицей и кетчупом, затем положите лист салата, котлету, кольца помидора и закройте верхней половинкой булочки.",
                    "5. Подавайте бургеры горячими с картофельными чипсами или картофельным пюре."
                ),
                imageUrl = "burger-hamburger.png"
            ),
            Recipe(
                id = 1,
                title = "Чизбургер с беконом",
                description = "Delicious cheeseburger with crispy bacon and melted cheese. Great combination for spicy food lovers",
                ingredients = listOf(
                    Ingredient("0.4", "кг", "Говяжий фарш"),
                    Ingredient("4.0", "шт", "Ломтика бекона"),
                    Ingredient("4.0", "шт", "Ломтика сыра чеддер"),
                    Ingredient("4.0", "шт", "Булочки для бургера"),
                    Ingredient("1.0", "шт", "Помидор, нарезанный"),
                    Ingredient("по вкусу", "", "Майонез и кетчуп")
                ),
                method = listOf(
                    "1. Обжарьте бекон на сковороде до хрустящей корочки, отложите на бумажное полотенце.",
                    "2. Сформируйте из фарша 4 котлеты, обжарьте с каждой стороны по 4 минуты.",
                    "3. За минуту до готовности положите на каждую котлету по ломтику сыра, чтобы он расплавился.",
                    "4. Соберите бургер: булочка, майонез, котлета с сыром, бекон, помидор, кетчуп.",
                    "5. Подавайте горячими."
                ),
                imageUrl = "burger-cheeseburger.png"
            )
        )
    }

    fun getRecipesByCategory(categoryId: Int): List<Recipe> {
        return if (categoryId == 0) {
            getRecipes()
        } else {
            emptyList()
        }
    }
}
