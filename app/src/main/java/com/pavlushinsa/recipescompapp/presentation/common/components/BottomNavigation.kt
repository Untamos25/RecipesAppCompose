package com.pavlushinsa.recipescompapp.presentation.common.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pavlushinsa.recipescompapp.R
import com.pavlushinsa.recipescompapp.presentation.common.theme.Dimens
import com.pavlushinsa.recipescompapp.presentation.common.theme.RecipesAppTheme

private const val PRESSED_BUTTON_SCALE = 0.95f
private const val DEFAULT_BUTTON_SCALE = 1f
private const val ANIMATION_DURATION_MS = 100

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
        val categoriesInteractionSource = remember { MutableInteractionSource() }
        val isCategoriesPressed by categoriesInteractionSource.collectIsPressedAsState()
        val categoriesScale by animateFloatAsState(
            targetValue = if (isCategoriesPressed) PRESSED_BUTTON_SCALE else DEFAULT_BUTTON_SCALE,
            animationSpec = tween(durationMillis = ANIMATION_DURATION_MS),
            label = "categoriesScaleAnimation"
        )

        Button(
            onClick = onCategoriesClick,
            shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
            interactionSource = categoriesInteractionSource,
            modifier = Modifier
                .weight(1f)
                .scale(categoriesScale)
        ) {
            Text(
                text = stringResource(R.string.title_categories).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.size(Dimens.paddingTiny))

        val favoritesInteractionSource = remember { MutableInteractionSource() }
        val isFavoritesPressed by favoritesInteractionSource.collectIsPressedAsState()
        val favoritesScale by animateFloatAsState(
            targetValue = if (isFavoritesPressed) PRESSED_BUTTON_SCALE else DEFAULT_BUTTON_SCALE,
            animationSpec = tween(durationMillis = ANIMATION_DURATION_MS),
            label = "favoritesScaleAnimation"
        )

        Button(
            onClick = onFavoriteClick,
            shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
            interactionSource = favoritesInteractionSource,
            modifier = Modifier
                .weight(1f)
                .scale(favoritesScale)
        ) {
            Text(
                text = stringResource(R.string.title_favorites).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.padding(end = Dimens.paddingMedium)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_heart_empty),
                contentDescription = stringResource(R.string.content_description_favorites_icon),
                tint = Color.White,
                modifier = Modifier
                    .size(Dimens.iconSizeMedium)
            )
        }
    }
}


@Preview(showBackground = true)
@Preview(showBackground = true, locale = "en", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BottomNavigationPreview() {
    RecipesAppTheme {
        BottomNavigation(
            onCategoriesClick = {},
            onFavoriteClick = {}
        )
    }
}
