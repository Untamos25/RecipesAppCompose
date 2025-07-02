package com.example.composeapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeapp.ui.model.CategoryUiModel
import com.example.composeapp.ui.navigation.BottomNavigation
import com.example.composeapp.ui.navigation.ScreenId
import com.example.composeapp.ui.screens.CategoriesScreen
import com.example.composeapp.ui.screens.FavoritesScreen
import com.example.composeapp.ui.screens.RecipesScreen
import com.example.composeapp.ui.theme.RecipesAppTheme

@Composable
fun RecipesApp() {
    RecipesAppTheme {

        var currentScreen by remember { mutableStateOf(ScreenId.CATEGORIES) }
        var selectedCategory by remember { mutableStateOf<CategoryUiModel?>(null) }

        Scaffold(
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = { currentScreen = ScreenId.CATEGORIES },
                    onFavoriteClick = { currentScreen = ScreenId.FAVORITES }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    ScreenId.CATEGORIES -> CategoriesScreen(
                        onCategoryClick = { category ->
                            selectedCategory = category
                            currentScreen = ScreenId.RECIPES
                        }
                    )
                    ScreenId.FAVORITES -> FavoritesScreen()
                    ScreenId.RECIPES -> RecipesScreen(selectedCategory)
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
