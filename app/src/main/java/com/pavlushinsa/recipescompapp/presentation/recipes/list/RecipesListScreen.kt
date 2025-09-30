package com.pavlushinsa.recipescompapp.presentation.recipes.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.pavlushinsa.recipescompapp.presentation.common.components.RecipeItem
import com.pavlushinsa.recipescompapp.presentation.common.components.ScreenContentWrapper
import com.pavlushinsa.recipescompapp.presentation.common.components.ScreenHeader
import com.pavlushinsa.recipescompapp.presentation.common.effects.TopBarVisibilityEffect
import com.pavlushinsa.recipescompapp.presentation.common.theme.Dimens
import com.pavlushinsa.recipescompapp.presentation.common.theme.RecipesAppTheme
import com.pavlushinsa.recipescompapp.presentation.recipes.list.PreviewData.loadingState
import com.pavlushinsa.recipescompapp.presentation.recipes.list.PreviewData.successState
import com.pavlushinsa.recipescompapp.presentation.recipes.list.model.RecipeCardUiModel
import com.pavlushinsa.recipescompapp.presentation.recipes.list.model.RecipesListUiState
import kotlinx.collections.immutable.toImmutableList

@Composable
fun RecipesListScreen(
    viewModel: RecipesListViewModel,
    onRecipeClick: (recipeId: Int) -> Unit,
    onTitleChanged: (String) -> Unit,
    onShowTopBarChanged: (Boolean) -> Unit
) {
    val recipesListUiState by viewModel.recipesListUiState.collectAsState()

    LaunchedEffect(recipesListUiState.categoryTitle) {
        if (recipesListUiState.categoryTitle.isNotBlank()) {
            onTitleChanged(recipesListUiState.categoryTitle)
        }
    }

    RecipesListContent(
        recipesListUiState = recipesListUiState,
        onRecipeClick = onRecipeClick,
        onRefresh = viewModel::onRefresh,
        onShowTopBarChanged = onShowTopBarChanged
    )
}

@Composable
private fun RecipesListContent(
    recipesListUiState: RecipesListUiState,
    onRecipeClick: (recipeId: Int) -> Unit,
    onRefresh: () -> Unit,
    onShowTopBarChanged: (Boolean) -> Unit
) {
    ScreenContentWrapper(
        isLoading = recipesListUiState.isLoading,
        isRefreshing = recipesListUiState.isRefreshing,
        onRefresh = onRefresh
    ) {
        RecipesListSuccessContent(
            recipesListUiState = recipesListUiState,
            onRecipeClick = onRecipeClick,
            onShowTopBarChanged = onShowTopBarChanged
        )
    }
}

@Composable
private fun RecipesListSuccessContent(
    recipesListUiState: RecipesListUiState,
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
                title = recipesListUiState.categoryTitle,
                imageUrl = recipesListUiState.categoryImageUrl,
                contentDescription = recipesListUiState.categoryTitle
            )
        }

        items(
            items = recipesListUiState.recipes,
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


@Preview(showBackground = true)
@Preview(showBackground = true, locale = "en", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RecipesContentPreview(
    @PreviewParameter(RecipesListUiStateProvider::class) state: RecipesListUiState
) {
    RecipesAppTheme {
        RecipesListContent(
            recipesListUiState = state,
            onRecipeClick = {},
            onRefresh = {},
            onShowTopBarChanged = {}
        )
    }
}

private class RecipesListUiStateProvider : PreviewParameterProvider<RecipesListUiState> {
    override val values = sequenceOf(successState, loadingState)
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
        }.toImmutableList(),
        isLoading = false
    )

    val loadingState = RecipesListUiState(
        isLoading = true
    )
}
