package com.example.composeapp.ui.navigation

import com.example.composeapp.data.CATEGORY_ID
import com.example.composeapp.data.RECIPE_ID

sealed class Destination(val route: String) {
    data object Categories : Destination("categories")
    data object Favorites : Destination("favorites")
    data object Recipes : Destination("recipes/{$CATEGORY_ID}") {
        fun createRoute(categoryId: Int) = "recipes/$categoryId"
    }
    data object RecipeDetail : Destination("recipe_detail/{$RECIPE_ID}") {
        fun createRoute(recipeId: Int) = "recipe_detail/$recipeId"
    }
}