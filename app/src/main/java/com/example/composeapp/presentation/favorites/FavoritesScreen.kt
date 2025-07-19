package com.example.composeapp.presentation.favorites

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeapp.R
import com.example.composeapp.presentation.common.components.ScreenHeader
import com.example.composeapp.presentation.common.theme.RecipesAppTheme

@Composable
fun FavoritesScreen(
    recipes: List<Any> = emptyList()
) {
    Column {
        ScreenHeader(
            titleResId = R.string.title_favorites,
            imageResId = R.drawable.img_header_favorites,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            if (recipes.isEmpty()) {
                Text(
                    text = stringResource(R.string.title_empty_favorites_list),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "Список рецептов, если он не пустой",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    textAlign = TextAlign.Center
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
fun FavoritesScreenLightPreview() {
    RecipesAppTheme {
        FavoritesScreen()
    }
}

@Preview(
    name = "DarkTheme",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun FavoritesScreenDarkPreview() {
    RecipesAppTheme {
        FavoritesScreen()
    }
}

@Preview(
    name = "LightTheme - With Content",
    showBackground = true,
)
@Composable
fun FavoritesScreenWithContentPreview() {
    RecipesAppTheme {
        FavoritesScreen(recipes = listOf(1))
    }
}