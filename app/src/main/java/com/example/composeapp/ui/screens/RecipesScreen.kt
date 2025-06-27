package com.example.composeapp.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeapp.R
import com.example.composeapp.ui.components.ScreenHeader
import com.example.composeapp.ui.theme.RecipesAppTheme

@Composable
fun RecipesScreen() {
    Column {
        ScreenHeader(
            titleResId = R.string.title_recipes,
            imageResId = R.drawable.img_placeholder,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) { }
    }
}


@Preview(
    name = "LightTheme",
    showBackground = true,
)
@Composable
fun RecipesScreenLightPreview() {
    RecipesAppTheme {
        RecipesScreen()
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
        RecipesScreen()
    }
}