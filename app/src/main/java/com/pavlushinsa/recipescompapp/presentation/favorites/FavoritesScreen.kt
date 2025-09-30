package com.pavlushinsa.recipescompapp.presentation.favorites

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.pavlushinsa.recipescompapp.R
import com.pavlushinsa.recipescompapp.presentation.common.components.RecipeItem
import com.pavlushinsa.recipescompapp.presentation.common.components.ScreenHeader
import com.pavlushinsa.recipescompapp.presentation.common.effects.TopBarVisibilityEffect
import com.pavlushinsa.recipescompapp.presentation.common.theme.Dimens
import com.pavlushinsa.recipescompapp.presentation.common.theme.RecipesAppTheme
import com.pavlushinsa.recipescompapp.presentation.favorites.PreviewData.emptyState
import com.pavlushinsa.recipescompapp.presentation.favorites.PreviewData.filledState
import com.pavlushinsa.recipescompapp.presentation.favorites.model.FavoritesUiState
import com.pavlushinsa.recipescompapp.presentation.recipes.list.model.RecipeCardUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onRecipeClick: (recipeId: Int) -> Unit,
    onTitleChanged: (String) -> Unit,
    onShowTopBarChanged: (Boolean) -> Unit
) {
    val favoritesUiState by viewModel.favoritesUiState.collectAsState()
    val screenTitle = stringResource(id = R.string.title_favorites)

    LaunchedEffect(Unit) {
        onTitleChanged(screenTitle)
    }

    FavoritesContent(
        favoritesUiState = favoritesUiState,
        onRecipeClick = onRecipeClick,
        onShowTopBarChanged = onShowTopBarChanged
    )
}

@Composable
private fun FavoritesContent(
    favoritesUiState: FavoritesUiState,
    onRecipeClick: (recipeId: Int) -> Unit,
    onShowTopBarChanged: (Boolean) -> Unit
) {
    if (favoritesUiState.recipes.isNotEmpty()) {
        FavoritesSuccessContent(
            recipes = favoritesUiState.recipes,
            onRecipeClick = onRecipeClick,
            onShowTopBarChanged = onShowTopBarChanged
        )
    } else {
        LaunchedEffect(Unit) {
            onShowTopBarChanged(false)
        }
        FavoritesEmptyState()
    }
}

@Composable
private fun FavoritesSuccessContent(
    recipes: ImmutableList<RecipeCardUiModel>,
    onRecipeClick: (recipeId: Int) -> Unit,
    onShowTopBarChanged: (Boolean) -> Unit
) {
    val lazyListState = rememberLazyListState()
    TopBarVisibilityEffect(lazyListState, onShowTopBarChanged)

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingLarge)
    ) {
        item {
            ScreenHeader(
                titleResId = R.string.title_favorites,
                imageResId = R.drawable.img_header_favorites,
                contentDescription = stringResource(id = R.string.content_description_favorites_header)
            )
        }
        items(
            items = recipes
        ) { recipe ->
            RecipeItem(
                imageUri = recipe.imageUrl,
                title = recipe.title,
                onClick = { onRecipeClick(recipe.id) },
                modifier = Modifier.padding(horizontal = Dimens.paddingLarge)
            )
        }
    }
}

@Composable
private fun FavoritesEmptyState() {
    Column(modifier = Modifier.fillMaxSize()) {
        ScreenHeader(
            titleResId = R.string.title_favorites,
            imageResId = R.drawable.img_header_favorites,
            contentDescription = stringResource(id = R.string.content_description_favorites_header)
        )
        CenteredMessage(
            textResId = R.string.title_empty_favorites_list,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
private fun CenteredMessage(
    @StringRes textResId: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
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
        FavoritesContent(
            favoritesUiState = state,
            onRecipeClick = {},
            onShowTopBarChanged = {})
    }
}

private class FavoritesUiStateProvider : PreviewParameterProvider<FavoritesUiState> {
    override val values = sequenceOf(filledState, emptyState)
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
}
