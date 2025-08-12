package com.example.composeapp.presentation.favorites

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.composeapp.R
import com.example.composeapp.presentation.common.components.RecipeItem
import com.example.composeapp.presentation.common.components.ScreenHeader
import com.example.composeapp.presentation.common.theme.Dimens
import com.example.composeapp.presentation.common.theme.RecipesAppTheme
import com.example.composeapp.presentation.favorites.PreviewData.emptyState
import com.example.composeapp.presentation.favorites.PreviewData.errorState
import com.example.composeapp.presentation.favorites.PreviewData.filledState
import com.example.composeapp.presentation.favorites.model.FavoritesUiState
import com.example.composeapp.presentation.recipes.list.model.RecipeCardUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onRecipeClick: (recipeId: Int) -> Unit
) {
    val favoritesUiState by viewModel.favoritesUiState.collectAsState()

    FavoritesContent(
        favoritesUiState = favoritesUiState,
        onRecipeClick = onRecipeClick
    )
}

@Composable
private fun FavoritesContent(
    favoritesUiState: FavoritesUiState,
    onRecipeClick: (recipeId: Int) -> Unit
) {
    Column {
        ScreenHeader(
            titleResId = R.string.title_favorites,
            imageResId = R.drawable.img_header_favorites,
        )

        when {
            favoritesUiState.isError -> {
                CenteredMessage(textResId = R.string.title_favorites_read_error)
            }
            favoritesUiState.recipes.isEmpty() -> {
                CenteredMessage(textResId = R.string.title_empty_favorites_list)
            }
            else -> {
                FavoritesList(
                    recipes = favoritesUiState.recipes,
                    onRecipeClick = onRecipeClick
                )
            }
        }
    }
}

@Composable
private fun FavoritesList(
    recipes: ImmutableList<RecipeCardUiModel>,
    onRecipeClick: (recipeId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(Dimens.paddingLarge),
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingSmall)
    ) {
        items(items = recipes) { recipe ->
            RecipeItem(
                imageUri = recipe.imageUrl,
                title = recipe.title,
                onClick = { onRecipeClick(recipe.id) }
            )
        }
    }
}

@Composable
private fun CenteredMessage(
    @StringRes textResId: Int
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(textResId),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "en", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FavoritesContentPreview(
    @PreviewParameter(FavoritesUiStateProvider::class) state: FavoritesUiState
) {
    RecipesAppTheme {
        FavoritesContent(favoritesUiState = state, onRecipeClick = {})
    }
}

private class FavoritesUiStateProvider : PreviewParameterProvider<FavoritesUiState> {
    override val values = sequenceOf(filledState, emptyState, errorState)
}

private object PreviewData {
    private val previewRecipe = RecipeCardUiModel(
        id = 0,
        title = "Название рецепта",
        imageUrl = ""
    )

    val filledState = FavoritesUiState(
        recipes = List(10) {
            previewRecipe.copy(
                id = it,
                title = "${previewRecipe.title} #$it"
            )
        }.toImmutableList()
    )

    val emptyState = FavoritesUiState(
        recipes = emptyList<RecipeCardUiModel>().toImmutableList()
    )

    val errorState = FavoritesUiState(
        isError = true
    )
}
