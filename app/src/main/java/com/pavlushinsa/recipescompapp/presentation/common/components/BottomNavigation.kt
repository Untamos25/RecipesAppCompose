package com.pavlushinsa.recipescompapp.presentation.common.components

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
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
import com.pavlushinsa.recipescompapp.presentation.common.components.ButtonAnimations.ANIMATION_DURATION_MS
import com.pavlushinsa.recipescompapp.presentation.common.components.ButtonAnimations.DEFAULT_BUTTON_SCALE
import com.pavlushinsa.recipescompapp.presentation.common.components.ButtonAnimations.PRESSED_BUTTON_SCALE
import com.pavlushinsa.recipescompapp.presentation.common.theme.Dimens
import com.pavlushinsa.recipescompapp.presentation.common.theme.RecipesAppTheme

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
        AnimatedNavButton(
            onClick = onCategoriesClick,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.title_categories).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.size(Dimens.paddingTiny))

        AnimatedNavButton(
            onClick = onFavoriteClick,
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
            modifier = Modifier.weight(1f)
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
                modifier = Modifier.size(Dimens.iconSizeMedium)
            )
        }
    }
}

@Composable
private fun RowScope.AnimatedNavButton(
    colors: ButtonColors,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,

    ) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) PRESSED_BUTTON_SCALE else DEFAULT_BUTTON_SCALE,
        animationSpec = tween(durationMillis = ANIMATION_DURATION_MS),
        label = "scaleAnimation"
    )

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(Dimens.cornerRadiusMedium),
        colors = colors,
        interactionSource = interactionSource,
        modifier = modifier.scale(scale),
        content = content
    )
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

private object ButtonAnimations {
    const val PRESSED_BUTTON_SCALE = 0.95f
    const val DEFAULT_BUTTON_SCALE = 1f
    const val ANIMATION_DURATION_MS = 100
}
