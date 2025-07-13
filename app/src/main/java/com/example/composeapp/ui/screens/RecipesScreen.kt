package com.example.composeapp.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeapp.R
import com.example.composeapp.data.model.mapper.toUiModel
import com.example.composeapp.data.repository.RecipesRepositoryStub
import com.example.composeapp.data.repository.RecipesRepositoryStub.getRecipesByCategoryId
import com.example.composeapp.ui.components.RecipeItem
import com.example.composeapp.ui.components.ScreenHeader
import com.example.composeapp.ui.model.RecipeUiModel
import com.example.composeapp.ui.theme.Dimens
import com.example.composeapp.ui.theme.RecipesAppTheme

@Composable
fun RecipesScreen(
    categoryId: Int,
    onRecipeClick: (recipe: RecipeUiModel) -> Unit
) {

    val category = remember(categoryId) {
        RecipesRepositoryStub.getCategories()
            .find { it.id == categoryId }
            ?.toUiModel()
    }

    if (category == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.title_category_not_found))
        }
        return
    }

    var recipes by remember(category.id) { mutableStateOf<List<RecipeUiModel>>(emptyList()) }

    LaunchedEffect(category.id) {
        recipes = getRecipesByCategoryId(category.id).map { it.toUiModel() }
    }

    Column {
        ScreenHeader(
            title = category.title,
            imageUrl = category.imageUrl,
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(Dimens.paddingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
        ) {
            items(items = recipes) { recipe ->
                RecipeItem(
                    imageUri = recipe.imageUrl,
                    title = recipe.title,
                    onClick = {onRecipeClick(recipe)}
                )
            }
        }
    }
}

@Preview(
    name = "LightTheme",
    showBackground = true,
)
@Composable
fun RecipesScreenLightPreview() {
    RecipesAppTheme {
        RecipesScreen(categoryId = 0, onRecipeClick = {})
    }
}

@Preview(
    name = "DarkTheme",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun RecipesScreenDarkPreview() {
    RecipesAppTheme {
        RecipesScreen(categoryId = 0, onRecipeClick = {})
    }
}

@Preview(
    name = "CategoryNotFound",
    showBackground = true,
)
@Composable
fun CategoryNotFoundPreview() {
    RecipesAppTheme {
        RecipesScreen(categoryId = -1, onRecipeClick = {})
    }
}
