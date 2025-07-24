package com.example.composeapp.presentation.common.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.composeapp.presentation.categories.CategoriesScreen
import com.example.composeapp.presentation.common.navigation.Destination.Companion.CATEGORY_ID
import com.example.composeapp.presentation.common.navigation.Destination.Companion.RECIPE_ID
import com.example.composeapp.presentation.favorites.FavoritesScreen
import com.example.composeapp.presentation.recipes.detail.RecipeDetailsScreen
import com.example.composeapp.presentation.recipes.list.RecipesScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = Destination.Categories.route
        ) {
            composable(route = Destination.Favorites.route) {
                FavoritesScreen()
            }

            composable(route = Destination.Categories.route) {
                CategoriesScreen(
                    viewModel = hiltViewModel(),
                    onCategoryClick = { categoryId ->
                        navController.navigate(Destination.Recipes.createRoute(categoryId))
                    }
                )
            }

            composable(
                route = Destination.Recipes.route,
                arguments = listOf(navArgument(CATEGORY_ID) { type = NavType.IntType })
            ) {
                RecipesScreen(
                    viewModel = hiltViewModel(),
                    onRecipeClick = { recipeId ->
                        navController.navigate(Destination.RecipeDetail.createRoute(recipeId))
                    }
                )
            }

            composable(
                route = Destination.RecipeDetail.route,
                arguments = listOf(navArgument(RECIPE_ID) { type = NavType.IntType })
            ) {
                RecipeDetailsScreen(
                    viewModel = hiltViewModel()
                )
            }
        }
    }
}
