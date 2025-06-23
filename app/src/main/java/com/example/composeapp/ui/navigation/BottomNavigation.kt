package com.example.composeapp.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeapp.R
import com.example.composeapp.ui.theme.RecipesAppTheme
import com.example.composeapp.ui.theme.recipesAppTypography

@Composable
fun BottomNavigation(
    onCategoriesClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(
                top = 8.dp,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    ) {
        Button(
            onClick = onCategoriesClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = "КАТЕГОРИИ",
                style = recipesAppTypography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.size(4.dp))

        Button(
            onClick = onFavoriteClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = "ИЗБРАННОЕ",
                style = recipesAppTypography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.padding(end = 10.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_heart_empty),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}

@Preview(
    name = "LightTheme",
    showBackground = true
)
@Composable
fun BottomNavigationLightPreview() {
    RecipesAppTheme {
        BottomNavigation(
            onCategoriesClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(
    name = "DarkTheme",
    showBackground = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun BottomNavigationDarkPreview() {
    RecipesAppTheme {
        BottomNavigation(
            onCategoriesClick = {},
            onFavoriteClick = {}
        )
    }
}
