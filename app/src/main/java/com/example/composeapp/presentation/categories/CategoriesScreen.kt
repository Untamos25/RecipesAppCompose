package com.example.composeapp.presentation.categories

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.composeapp.R
import com.example.composeapp.presentation.categories.PreviewData.successState
import com.example.composeapp.presentation.categories.components.CategoryItem
import com.example.composeapp.presentation.categories.model.CategoriesUiState
import com.example.composeapp.presentation.categories.model.CategoryUiModel
import com.example.composeapp.presentation.common.components.ScreenHeader
import com.example.composeapp.presentation.common.theme.Dimens
import com.example.composeapp.presentation.common.theme.RecipesAppTheme

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel,
    onCategoryClick: (categoryId: Int) -> Unit
) {
    val categoriesUiState by viewModel.categoriesUiState.collectAsState()

    CategoriesContent(
        categoriesUiState = categoriesUiState,
        onCategoryClick = onCategoryClick
    )
}

@Composable
private fun CategoriesContent(
    categoriesUiState: CategoriesUiState,
    onCategoryClick: (categoryId: Int) -> Unit
) {
    Column {
        ScreenHeader(
            titleResId = R.string.title_categories,
            imageResId = R.drawable.img_header_categories,
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(Dimens.paddingLarge),
            horizontalArrangement = Arrangement.spacedBy(Dimens.paddingLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingLarge)
        ) {
            items(items = categoriesUiState.categories) { category ->
                CategoryItem(
                    imageUri = category.imageUrl,
                    title = category.title,
                    description = category.description,
                    onClick = { onCategoryClick(category.id) }
                )
            }
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
            categoriesUiState = state
        )
    }
}

private class CategoriesUiStateProvider : PreviewParameterProvider<CategoriesUiState> {
    override val values = sequenceOf(successState)
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
                description = "${previewCategory.description} #$it"
            )
        }
    )
}
