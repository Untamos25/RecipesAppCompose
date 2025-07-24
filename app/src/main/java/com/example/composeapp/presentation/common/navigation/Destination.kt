package com.example.composeapp.presentation.common.navigation

sealed class Destination(val route: String) {

    companion object {
        const val CATEGORY_ID = "categoryId"
        const val RECIPE_ID = "recipeId"
    }

    data object Categories : Destination("categories")
    data object Favorites : Destination("favorites")
    data object Recipes : Destination("recipes/{$CATEGORY_ID}") {
        fun createRoute(categoryId: Int) = "recipes/$categoryId"
    }

    data object RecipeDetail : Destination("recipe_detail/{$RECIPE_ID}") {
        fun createRoute(recipeId: Int) = "recipe_detail/$recipeId"
    }
}
