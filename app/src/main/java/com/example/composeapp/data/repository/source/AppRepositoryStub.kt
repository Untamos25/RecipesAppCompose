package com.example.composeapp.data.repository.source

import com.example.composeapp.data.categories.model.CategoryDto
import com.example.composeapp.data.recipes.model.IngredientDto
import com.example.composeapp.data.recipes.model.RecipeDto

object AppRepositoryStub {
    private val categories = listOf(
        CategoryDto(
            id = 0,
            title = "Бургеры",
            description = "Рецепты всех популярных видов бургеров",
            imageUrl = "burger.png"
        ),
        CategoryDto(
            id = 1,
            title = "Десерты",
            description = "Самые вкусные рецепты десертов специально для вас",
            imageUrl = "dessert.png"
        ),
        CategoryDto(
            id = 2,
            title = "Пицца",
            description = "Пицца на любой вкус и цвет. Лучшая подборка для тебя",
            imageUrl = "pizza.png"
        ),
        CategoryDto(
            id = 3,
            title = "Рыба",
            description = "Печеная, жареная, сушеная, любая рыба на твой вкус",
            imageUrl = "fish.png"
        ),
        CategoryDto(
            id = 4,
            title = "Супы",
            description = "От классики до экзотики: мир в одной тарелке",
            imageUrl = "soup.png"
        ),
        CategoryDto(
            id = 5,
            title = "Салаты",
            description = "Хрустящий калейдоскоп под соусом вдохновения",
            imageUrl = "salad.png"
        )
    )

    private val recipes = listOf(
        RecipeDto(
            id = 0,
            title = "Классический бургер с говядиной",
            ingredients = listOf(
                IngredientDto(quantity = "0.5", unitOfMeasure = "кг", description = "говяжий фарш"),
                IngredientDto(
                    quantity = "1.0",
                    unitOfMeasure = "шт",
                    description = "луковица, мелко нарезанная"
                ),
                IngredientDto(
                    quantity = "2.0",
                    unitOfMeasure = "зубч",
                    description = "чеснок, измельченный"
                ),
                IngredientDto(
                    quantity = "4.0",
                    unitOfMeasure = "шт",
                    description = "булочки для бургера"
                ),
                IngredientDto(quantity = "4.0", unitOfMeasure = "шт", description = "листа салата"),
                IngredientDto(
                    quantity = "1.0",
                    unitOfMeasure = "шт",
                    description = "помидор, нарезанный кольцами"
                ),
                IngredientDto(quantity = "2.0", unitOfMeasure = "ст. л.", description = "горчица"),
                IngredientDto(quantity = "2.0", unitOfMeasure = "ст. л.", description = "кетчуп"),
                IngredientDto(
                    quantity = "по вкусу",
                    unitOfMeasure = "",
                    description = "соль и черный перец"
                )
            ),
            method = listOf(
                "1. В глубокой миске смешайте говяжий фарш, лук, чеснок, соль и перец. Разделите фарш на 4 равные части и сформируйте котлеты.",
                "2. Разогрейте сковороду на среднем огне. Обжаривайте котлеты с каждой стороны в течение 4-5 минут или до желаемой степени прожарки.",
                "3. В то время как котлеты готовятся, подготовьте булочки. Разрежьте их пополам и обжарьте на сковороде до золотистой корочки.",
                "4. Смазать нижние половинки булочек горчицей и кетчупом, затем положите лист салата, котлету, кольца помидора и закройте верхней половинкой булочки.",
                "5. Подавайте бургеры горячими с картофельными чипсами или картофельным пюре."
            ),
            imageUrl = "burger_classic.png"
        )
    )

    fun getCategories(): List<CategoryDto> = categories

    fun getCategoryById(categoryId: Int): CategoryDto? {
        return categories.find { it.id == categoryId }
    }

    fun getRecipesByCategoryId(categoryId: Int): List<RecipeDto> {
        return when (categoryId) {
            0 -> recipes
            else -> emptyList()
        }
    }

    fun getRecipeById(recipeId: Int): RecipeDto? {
        return recipes.find { it.id == recipeId }
    }
}
