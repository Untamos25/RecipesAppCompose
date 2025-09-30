package com.pavlushinsa.recipescompapp.presentation.recipes.detail.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.pavlushinsa.recipescompapp.R
import com.pavlushinsa.recipescompapp.presentation.common.constants.SliderConstants.MAX_PORTIONS
import com.pavlushinsa.recipescompapp.presentation.common.constants.SliderConstants.MIN_PORTIONS
import com.pavlushinsa.recipescompapp.presentation.common.constants.SliderConstants.PORTIONS_SLIDER_STEPS
import com.pavlushinsa.recipescompapp.presentation.common.constants.SliderConstants.SLIDER_THUMB_SCALE_DEFAULT
import com.pavlushinsa.recipescompapp.presentation.common.constants.SliderConstants.SLIDER_THUMB_SCALE_DRAGGED
import com.pavlushinsa.recipescompapp.presentation.common.theme.Dimens
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.formatter.IngredientFormatter
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.formatter.QuantityFormatter
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.model.IngredientUiModel

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
