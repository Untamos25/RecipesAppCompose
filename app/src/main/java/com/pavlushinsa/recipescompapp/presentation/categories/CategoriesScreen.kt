package com.pavlushinsa.recipescompapp.presentation.categories

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.pavlushinsa.recipescompapp.R
import com.pavlushinsa.recipescompapp.presentation.categories.PreviewData.loadingState
import com.pavlushinsa.recipescompapp.presentation.categories.PreviewData.successState
import com.pavlushinsa.recipescompapp.presentation.categories.components.CategoryItem
import com.pavlushinsa.recipescompapp.presentation.categories.model.CategoriesUiState
import com.pavlushinsa.recipescompapp.presentation.categories.model.CategoryUiModel
import com.pavlushinsa.recipescompapp.presentation.common.components.ScreenContentWrapper
import com.pavlushinsa.recipescompapp.presentation.common.components.ScreenHeader
import com.pavlushinsa.recipescompapp.presentation.common.effects.TopBarVisibilityEffect
import com.pavlushinsa.recipescompapp.presentation.common.theme.Dimens
import com.pavlushinsa.recipescompapp.presentation.common.theme.RecipesAppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private const val COLUMN_COUNT = 2

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel,
    onCategoryClick: (categoryId: Int) -> Unit,
    onTitleChange: (String) -> Unit,
    onShowTopBarChange: (Boolean) -> Unit
) {
    val categoriesUiState by viewModel.categoriesUiState.collectAsState()
    val screenTitle = stringResource(id = R.string.title_categories)

    LaunchedEffect(Unit) {
        onTitleChange(screenTitle)
    }

    CategoriesContent(
        categoriesUiState = categoriesUiState,
        onCategoryClick = onCategoryClick,
        onRefresh = viewModel::onRefresh,
        onShowTopBarChange = onShowTopBarChange
    )
}

@Composable
private fun CategoriesContent(
    categoriesUiState: CategoriesUiState,
    onCategoryClick: (categoryId: Int) -> Unit,
    onRefresh: () -> Unit,
    onShowTopBarChange: (Boolean) -> Unit
) {
    ScreenContentWrapper(
        isLoading = categoriesUiState.isLoading,
        isRefreshing = categoriesUiState.isRefreshing,
        onRefresh = onRefresh
    ) {
        CategoriesSuccessContent(
            categories = categoriesUiState.categories,
            onCategoryClick = onCategoryClick,
            onShowTopBarChange = onShowTopBarChange
        )
    }
}

@Composable
private fun CategoriesSuccessContent(
    categories: ImmutableList<CategoryUiModel>,
    onCategoryClick: (categoryId: Int) -> Unit,
    onShowTopBarChange: (Boolean) -> Unit
) {
    val lazyGridState = rememberLazyGridState()
    TopBarVisibilityEffect(lazyGridState, onShowTopBarChange)

    LazyVerticalGrid(
        state = lazyGridState,
        columns = GridCells.Fixed(COLUMN_COUNT),
        horizontalArrangement = Arrangement.spacedBy(-Dimens.paddingLarge),
        verticalArrangement = Arrangement.spacedBy(Dimens.paddingLarge)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            ScreenHeader(
                titleResId = R.string.title_categories,
                imageResId = R.drawable.img_header_categories,
                contentDescription = stringResource(R.string.content_description_categories_header)
            )
        }

        items(items = categories) { category ->
            CategoryItem(
                imageUri = category.imageUrl,
                title = category.title,
                description = category.description,
                onClick = { onCategoryClick(category.id) },
                modifier = Modifier.padding(horizontal = Dimens.paddingLarge)
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "en", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun CategoriesContentPreview(
    @PreviewParameter(CategoriesUiStateProvider::class) state: CategoriesUiState
) {
    RecipesAppTheme {
        CategoriesContent(
            onCategoryClick = {},
            categoriesUiState = state,
            onRefresh = {},
            onShowTopBarChange = {}
        )
    }
}

private class CategoriesUiStateProvider : PreviewParameterProvider<CategoriesUiState> {
    override val values = sequenceOf(successState, loadingState)
}

private object PreviewData {
    private val previewCategory = CategoryUiModel(
        id = 0,
        title = "Категория",
        imageUrl = "",
        description = "Описание категории на несколько строк"
    )

    val successState = CategoriesUiState(
        categories = List(8) {
            previewCategory.copy(
                id = it,
                title = "${previewCategory.title} #$it",
                description = "${previewCategory.description} #$it",
            )
        }.toImmutableList(),
        isLoading = false
    )

    val loadingState = CategoriesUiState(
        isLoading = true
    )
}
