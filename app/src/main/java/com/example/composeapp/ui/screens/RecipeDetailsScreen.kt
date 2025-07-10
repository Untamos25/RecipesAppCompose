package com.example.composeapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeapp.ui.components.ScreenHeader
import com.example.composeapp.ui.model.RecipeUiModel
import com.example.composeapp.ui.theme.RecipesAppTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RecipeDetailsScreen(recipe: RecipeUiModel?) {
    recipe?.let {
        Column {
            ScreenHeader(
                title = it.title,
                imageUrl = it.imageUrl
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailsScreenPreview() {
    RecipesAppTheme {
        RecipeDetailsScreen(
            recipe = RecipeUiModel(
                id = 0,
                title = "Классический бургер",
                imageUrl = "file:///android_asset/burger_classic.png",
                ingredients = persistentListOf(),
                method = persistentListOf()
            )
        )
    }
}