package com.example.composeapp.presentation.common

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.composeapp.presentation.common.components.BottomNavigation
import com.example.composeapp.presentation.common.navigation.AppNavigation
import com.example.composeapp.presentation.common.navigation.Destination
import com.example.composeapp.presentation.common.theme.RecipesAppTheme

@Composable
fun RecipesApp() {
    RecipesAppTheme {
        val navController = rememberNavController()

        RecipesAppContent(
            onCategoriesClick = { navController.navigate(Destination.Categories.route) },
            onFavoriteClick = { navController.navigate(Destination.Favorites.route) },
            content = { modifier ->
                AppNavigation(
                    navController = navController,
                    modifier = modifier
                )
            }
        )
    }
}

@Composable
private fun RecipesAppContent(
    onCategoriesClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    content: @Composable (modifier: Modifier) -> Unit
) {
    RecipesAppTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = onCategoriesClick,
                    onFavoriteClick = onFavoriteClick
                )
            }
        ) { innerPadding ->
            content(Modifier.padding(innerPadding))
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "en", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RecipesAppContentPreview() {
    RecipesAppTheme {
        RecipesAppContent(
            onCategoriesClick = {},
            onFavoriteClick = {},
            content = { modifier ->
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .border(width = 2.dp, color = Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    Text("NavHost")
                }
            }
        )
    }
}
