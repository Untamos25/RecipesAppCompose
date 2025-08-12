package com.example.composeapp.presentation.recipes.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.composeapp.R
import com.example.composeapp.presentation.common.components.RecipeItem
import com.example.composeapp.presentation.common.components.ScreenHeader
import com.example.composeapp.presentation.common.theme.Dimens
import com.example.composeapp.presentation.common.theme.RecipesAppTheme
import com.example.composeapp.presentation.recipes.list.PreviewData.errorState
import com.example.composeapp.presentation.recipes.list.PreviewData.successState
import com.example.composeapp.presentation.recipes.list.model.RecipeCardUiModel
import com.example.composeapp.presentation.recipes.list.model.RecipesListUiState
import kotlinx.collections.immutable.toImmutableList

@Composable
fun RecipesListScreen(
    viewModel: RecipesListViewModel,
    onRecipeClick: (recipeId: Int) -> Unit
) {
    val recipesListUiState by viewModel.recipesListUiState.collectAsState()

    RecipesListContent(
        recipesListUiState = recipesListUiState,
        onRecipeClick = onRecipeClick
    )
}

@Composable
private fun RecipesListContent(
    recipesListUiState: RecipesListUiState,
    onRecipeClick: (recipeId: Int) -> Unit
) {
    if (recipesListUiState.isError) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(R.string.title_category_not_found))
        }
        return
    }

    Column {
        ScreenHeader(
            title = recipesListUiState.categoryTitle,
            imageUrl = recipesListUiState.categoryImageUrl,
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(Dimens.paddingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
        ) {
            items(items = recipesListUiState.recipes) { recipe ->
                RecipeItem(
                    imageUri = recipe.imageUrl,
                    title = recipe.title,
                    onClick = { onRecipeClick(recipe.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "en", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RecipesContentPreview(
    @PreviewParameter(RecipesListUiStateProvider::class) state: RecipesListUiState
) {
    RecipesAppTheme {
        RecipesListContent(recipesListUiState = state, onRecipeClick = {})
    }
}

private class RecipesListUiStateProvider : PreviewParameterProvider<RecipesListUiState> {
    override val values = sequenceOf(successState, errorState)
}

private object PreviewData {
    private val previewRecipe = RecipeCardUiModel(
        id = 0,
        title = "Название рецепта",
        imageUrl = ""
    )

    val successState = RecipesListUiState(
        categoryTitle = "Категория #0",
        categoryImageUrl = "",
        recipes = List(10) {
            previewRecipe.copy(
                id = it,
                title = "${previewRecipe.title} #$it"
            )
        }.toImmutableList()
    )

    val errorState = RecipesListUiState(
        isError = true
    )
}
