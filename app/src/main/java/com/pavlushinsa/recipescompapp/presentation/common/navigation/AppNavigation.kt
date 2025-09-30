package com.pavlushinsa.recipescompapp.presentation.common.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.pavlushinsa.recipescompapp.presentation.categories.CategoriesScreen
import com.pavlushinsa.recipescompapp.presentation.common.navigation.Destination.Companion.CATEGORY_ID
import com.pavlushinsa.recipescompapp.presentation.common.navigation.Destination.Companion.RECIPE_ID
import com.pavlushinsa.recipescompapp.presentation.favorites.FavoritesScreen
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.RecipeDetailsScreen
import com.pavlushinsa.recipescompapp.presentation.recipes.list.RecipesListScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onTitleChanged: (String) -> Unit,
    onShowTopBarChanged: (Boolean) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = Destination.Categories.route
        ) {
            bottomBarComposable(route = Destination.Favorites.route) {
                FavoritesScreen(
                    viewModel = hiltViewModel(),
                    onRecipeClick = { recipeId ->
                        navController.navigate(Destination.RecipeDetail.createRoute(recipeId))
                    },
                    onTitleChanged = onTitleChanged,
                    onShowTopBarChanged = onShowTopBarChanged
                )
            }


            bottomBarComposable(route = Destination.Categories.route) {
                CategoriesScreen(
                    viewModel = hiltViewModel(),
                    onCategoryClick = { categoryId ->
                        navController.navigate(Destination.Recipes.createRoute(categoryId))
                    },
                    onTitleChanged = onTitleChanged,
                    onShowTopBarChanged = onShowTopBarChanged
                )
            }

            animatedComposable(
                route = Destination.Recipes.route,
                arguments = listOf(navArgument(CATEGORY_ID) { type = NavType.IntType })
            ) {
                RecipesListScreen(
                    viewModel = hiltViewModel(),
                    onRecipeClick = { recipeId ->
                        navController.navigate(Destination.RecipeDetail.createRoute(recipeId))
                    },
                    onTitleChanged = onTitleChanged,
                    onShowTopBarChanged = onShowTopBarChanged
                )
            }

            animatedComposable(
                route = Destination.RecipeDetail.route,
                arguments = listOf(navArgument(RECIPE_ID) { type = NavType.IntType })
            ) {
                RecipeDetailsScreen(
                    viewModel = hiltViewModel(),
                    onTitleChanged = onTitleChanged,
                    onShowTopBarChanged = onShowTopBarChanged
                )
            }
        }
    }
}
