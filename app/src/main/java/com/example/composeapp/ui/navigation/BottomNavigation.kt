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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeapp.R
import com.example.composeapp.ui.theme.Dimens
import com.example.composeapp.ui.theme.RecipesAppTheme

@Composable
fun BottomNavigation(
    onCategoriesClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.paddingLarge)
            .padding(
                top = Dimens.paddingSmall,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            )
    ) {
        Button(
            onClick = onCategoriesClick,
            shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.title_categories).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.size(Dimens.paddingTiny))

        Button(
            onClick = onFavoriteClick,
            shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.title_favorites).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.padding(end = Dimens.paddingMedium)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_heart_empty),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .size(Dimens.iconSizeMedium)
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
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    locale = "en"
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
