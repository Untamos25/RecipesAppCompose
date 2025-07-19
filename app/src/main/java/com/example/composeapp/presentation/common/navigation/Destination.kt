package com.example.composeapp.presentation.common.navigation

import com.example.composeapp.presentation.common.UIConstants.CATEGORY_ID
import com.example.composeapp.presentation.common.UIConstants.RECIPE_ID


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