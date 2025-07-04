package com.example.composeapp.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeapp.R
import com.example.composeapp.data.model.mapper.toUiModel
import com.example.composeapp.data.repository.RecipesRepositoryStub
import com.example.composeapp.ui.components.CategoryItem
import com.example.composeapp.ui.components.ScreenHeader
import com.example.composeapp.ui.model.CategoryUiModel
import com.example.composeapp.ui.theme.Dimens
import com.example.composeapp.ui.theme.RecipesAppTheme

@Composable
fun CategoriesScreen(onCategoryClick: (category: CategoryUiModel) -> Unit) {

    val categories = RecipesRepositoryStub.getCategories().map { it.toUiModel() }

    Column {
        ScreenHeader(
            titleResId = R.string.title_categories,
            imageResId = R.drawable.img_header_categories,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(Dimens.paddingLarge),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(Dimens.paddingLarge),
                verticalArrangement = Arrangement.spacedBy(Dimens.paddingLarge)
            ) {
                items(items = categories) { category ->
                    CategoryItem(
                        imageUri = category.imageUrl,
                        title = category.title,
                        description = category.description,
                        onClick = { onCategoryClick(category) }
                    )
                }
            }
        }
    }
}

@Preview(
    name = "LightTheme",
    showBackground = true,
)
@Composable
fun CategoriesScreenLightPreview() {
    RecipesAppTheme {
        CategoriesScreen(onCategoryClick = {})
    }
}

@Preview(
    name = "DarkTheme",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun CategoriesScreenDarkPreview() {
    RecipesAppTheme {
        CategoriesScreen(onCategoryClick = {})
    }
}
