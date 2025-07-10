package com.example.composeapp.ui

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
import com.example.composeapp.data.CATEGORY_ID
import com.example.composeapp.data.RECIPE_ARG
import com.example.composeapp.data.RECIPE_ID
import com.example.composeapp.ui.model.RecipeUiModel
import com.example.composeapp.ui.navigation.BottomNavigation
import com.example.composeapp.ui.navigation.Destination
import com.example.composeapp.ui.screens.CategoriesScreen
import com.example.composeapp.ui.screens.FavoritesScreen
import com.example.composeapp.ui.screens.RecipeDetailsScreen
import com.example.composeapp.ui.screens.RecipesScreen
import com.example.composeapp.ui.theme.RecipesAppTheme

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
                            onRecipeClick = { recipe ->
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    key = RECIPE_ARG,
                                    value = recipe
                                )
                                navController.navigate(
                                    Destination.RecipeDetail.createRoute(recipe.id)
                                )
                            }
                        )
                    }

                    composable(
                        route = Destination.RecipeDetail.route,
                        arguments = listOf(navArgument(RECIPE_ID) { type = NavType.IntType })
                    ) {
                        val recipe = navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.get<RecipeUiModel>(RECIPE_ARG)
                        RecipeDetailsScreen(recipe = recipe)
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
