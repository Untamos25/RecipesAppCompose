package com.pavlushinsa.recipescompapp.presentation.recipes.detail.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.pavlushinsa.recipescompapp.R
import com.pavlushinsa.recipescompapp.presentation.common.theme.Dimens
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.components.FavoriteIconAnimationConstants.CROSSFADE_DURATION_MS
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.components.FavoriteIconAnimationConstants.DEFAULT_SCALE
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.components.FavoriteIconAnimationConstants.POST_CLICK_ANIMATION_DURATION_MS
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.components.FavoriteIconAnimationConstants.POST_CLICK_SHRINK_SCALE
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.components.FavoriteIconAnimationConstants.PRESSED_SCALE
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.components.SliderConstants.MAX_PORTIONS
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.components.SliderConstants.MIN_PORTIONS
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.components.SliderConstants.PORTIONS_SLIDER_STEPS
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.components.SliderConstants.SLIDER_THUMB_SCALE_DEFAULT
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.components.SliderConstants.SLIDER_THUMB_SCALE_DRAGGED
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.formatter.IngredientFormatter
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.formatter.QuantityFormatter
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.model.IngredientUiModel

@Composable
fun FavoriteIconButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale = remember { Animatable(DEFAULT_SCALE) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            scale.animateTo(
                targetValue = PRESSED_SCALE,
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            )
        }
    }

    LaunchedEffect(isFavorite) {
        if (!isPressed) {
            scale.animateTo(
                targetValue = POST_CLICK_SHRINK_SCALE,
                animationSpec = tween(POST_CLICK_ANIMATION_DURATION_MS)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        }
    }

    Crossfade(
        targetState = isFavorite,
        animationSpec = tween(durationMillis = CROSSFADE_DURATION_MS),
        modifier = modifier
            .scale(scale.value)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        label = "FavoriteIconCrossfade"
    ) { favoriteState ->
        val heartIcon = if (favoriteState) R.drawable.ic_heart else R.drawable.ic_heart_empty
        val contentDescriptionRes = if (favoriteState) {
            R.string.content_description_remove_from_favorites
        } else {
            R.string.content_description_add_to_favorites
        }

        Image(
            painter = painterResource(heartIcon),
            contentDescription = stringResource(contentDescriptionRes),
            modifier = Modifier.size(Dimens.iconSizeLarge)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PortionsSelector(
    portionsCount: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isDragged by interactionSource.collectIsDraggedAsState()
    val thumbScale by animateFloatAsState(
        targetValue =
            if (isDragged) SLIDER_THUMB_SCALE_DRAGGED
            else SLIDER_THUMB_SCALE_DEFAULT,
    )

    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.portions_count, portionsCount.toInt()),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondary,
        )
        Slider(
            value = portionsCount,
            onValueChange = onValueChange,
            valueRange = MIN_PORTIONS..MAX_PORTIONS,
            steps = PORTIONS_SLIDER_STEPS,
            interactionSource = interactionSource,
            thumb = {
                Box(
                    modifier = Modifier
                        .scale(thumbScale)
                        .size(
                            width = Dimens.portionsSliderThumbWidth,
                            height = Dimens.portionsSliderThumbHeight
                        )
                        .clip(RoundedCornerShape(Dimens.cornerRadiusExtraSmall))
                        .background(MaterialTheme.colorScheme.tertiary)
                )
            },
            track = { _ ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.portionsSliderTrackHeight)
                        .background(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(Dimens.cornerRadiusSmall)
                        )
                )
            }
        )
    }
}

@Composable
internal fun IngredientsList(
    ingredients: List<IngredientUiModel>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Dimens.paddingSmall)
            .clip(RoundedCornerShape(Dimens.cornerRadiusMedium))
            .background(MaterialTheme.colorScheme.surface)
            .padding(Dimens.paddingBig)
    ) {
        ingredients.forEachIndexed { index, ingredient ->
            IngredientItem(ingredient = ingredient)

            if (index < ingredients.lastIndex) {
                HorizontalDivider(
                    thickness = Dimens.dividerThickness,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.padding(vertical = Dimens.paddingSmall)
                )
            }
        }
    }
}

@Composable
internal fun IngredientItem(ingredient: IngredientUiModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = ingredient.description.uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.weight(1f)
        )

        val formattedQuantity = QuantityFormatter.format(ingredient.quantity)
        val formattedUnitOfMeasure =
            IngredientFormatter.formatUnitOfMeasure(
                quantity = ingredient.quantity,
                unitOfMeasure = ingredient.unitOfMeasure
            )

        Text(
            text = "$formattedQuantity $formattedUnitOfMeasure".uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(start = Dimens.paddingSmall)
        )
    }
}

@Composable
internal fun CookingMethodBlock(
    steps: List<String>,
    modifier: Modifier = Modifier
) {
    if (steps.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.cornerRadiusMedium))
                .background(MaterialTheme.colorScheme.surface)
                .padding(Dimens.paddingBig)
        ) {
            steps.forEachIndexed { index, stepDescription ->
                Text(
                    text = stepDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.fillMaxWidth(),
                )

                if (index < steps.lastIndex) {
                    HorizontalDivider(
                        thickness = Dimens.dividerThickness,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.padding(vertical = Dimens.paddingSmall)
                    )
                }
            }
        }
    }
}

private object SliderConstants {
    const val MIN_PORTIONS = 1f
    const val MAX_PORTIONS = 6f
    const val PORTIONS_SLIDER_STEPS = 4
    const val SLIDER_THUMB_SCALE_DEFAULT = 1.0f
    const val SLIDER_THUMB_SCALE_DRAGGED = 1.5f
}

private object FavoriteIconAnimationConstants {
    const val PRESSED_SCALE = 1.3f
    const val DEFAULT_SCALE = 1f
    const val POST_CLICK_SHRINK_SCALE = 0.9f
    const val POST_CLICK_ANIMATION_DURATION_MS = 100
    const val CROSSFADE_DURATION_MS = 300
}
