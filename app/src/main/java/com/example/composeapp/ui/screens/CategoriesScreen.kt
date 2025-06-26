package com.example.composeapp.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeapp.R
import com.example.composeapp.ui.components.ScreenHeader
import com.example.composeapp.ui.theme.RecipesAppTheme
import com.example.composeapp.ui.theme.recipesAppTypography

@Composable
fun CategoriesScreen() {
    Column {
        ScreenHeader(
            titleResId = R.string.categories,
            imageResId = R.drawable.categories,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.screen_categories),
                style = recipesAppTypography.displayLarge
            )
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
        CategoriesScreen()
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
        CategoriesScreen()
    }
}
