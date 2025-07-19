package com.example.composeapp.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composeapp.presentation.categories.CategoriesScreen
import com.example.composeapp.presentation.common.UIConstants.CATEGORY_ID
import com.example.composeapp.presentation.common.UIConstants.RECIPE_ID
import com.example.composeapp.presentation.common.navigation.BottomNavigation
import com.example.composeapp.presentation.common.navigation.Destination
import com.example.composeapp.presentation.common.theme.RecipesAppTheme
import com.example.composeapp.presentation.favorites.FavoritesScreen
import com.example.composeapp.presentation.recipes.detail.RecipeDetailsScreen
import com.example.composeapp.presentation.recipes.list.RecipesScreen

@Composable
fun RecipesApp() {
    RecipesAppTheme {

        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = { navController.navigate(Destination.Categories.route) },
                    onFavoriteClick = { navController.navigate(Destination.Favorites.route) }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Destination.Categories.route
                ) {
                    composable(route = Destination.Categories.route) {
                        CategoriesScreen { category ->
                            navController.navigate(Destination.Recipes.createRoute(category.id))
                        }
                    }

                    composable(route = Destination.Favorites.route) {
                        FavoritesScreen()
                    }

                    composable(
                        route = Destination.Recipes.route,
                        arguments = listOf(navArgument(CATEGORY_ID) { type = NavType.IntType })
                    ) { backStackEntry ->
                        val categoryId = backStackEntry.arguments?.getInt(CATEGORY_ID) ?: 0
                        RecipesScreen(
                            categoryId = categoryId,
                            onRecipeClick = { recipeId ->
                                navController.navigate(
                                    Destination.RecipeDetail.createRoute(recipeId)
                                )
                            }
                        )
                    }

                    composable(
                        route = Destination.RecipeDetail.route,
                        arguments = listOf(navArgument(RECIPE_ID) { type = NavType.IntType })
                    ) { backStackEntry ->
                        val recipeId = backStackEntry.arguments?.getInt(RECIPE_ID) ?: 0
                        RecipeDetailsScreen(recipeId = recipeId)
                    }
                }
            }
        }
    }
}

@Preview(
    name = "LightTheme",
    showBackground = true
)
@Composable
fun RecipesAppLightPreview() {
    RecipesAppTheme {
        RecipesApp()
    }
}

@Preview(
    name = "DarkTheme",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RecipesAppDarkPreview() {
    RecipesAppTheme {
        RecipesApp()
    }
}
