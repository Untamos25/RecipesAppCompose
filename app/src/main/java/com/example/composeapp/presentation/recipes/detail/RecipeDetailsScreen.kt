package com.example.composeapp.presentation.recipes.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.composeapp.R
import com.example.composeapp.data.common.source.RecipesRepositoryStub
import com.example.composeapp.data.recipes.mapper.toDomainModel
import com.example.composeapp.presentation.common.UIConstants
import com.example.composeapp.presentation.common.components.ScreenHeader
import com.example.composeapp.presentation.common.theme.Dimens
import com.example.composeapp.presentation.common.theme.RecipesAppTheme
import com.example.composeapp.presentation.recipes.mapper.toRecipeDetailUiModel
import com.example.composeapp.presentation.recipes.model.IngredientUiModel
import com.example.composeapp.presentation.recipes.model.RecipeUiModel
import java.text.DecimalFormat

@Composable
fun RecipeDetailsScreen(recipeId: Int) {

    val recipe = remember(recipeId) {
        RecipesRepositoryStub.getRecipeById(recipeId)
            ?.toDomainModel()
            ?.toRecipeDetailUiModel()
    }

    if (recipe != null) {
        RecipeDetailsContent(recipe = recipe)
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.title_recipe_not_found),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun RecipeDetailsContent(recipe: RecipeUiModel) {
    val initialPortions = UIConstants.INITIAL_PORTIONS
    var sliderPosition by remember { mutableFloatStateOf(initialPortions) }
    var calculatedIngredients by remember { mutableStateOf<List<IngredientUiModel>>(emptyList()) }

    LaunchedEffect(recipe, sliderPosition) {
        calculatedIngredients = recipe.ingredients.map { ingredient ->
            ingredient.copy(
                quantity = ingredient.quantity.toFloatOrNull()?.let { originalQuantity ->
                    DecimalFormat(UIConstants.INGREDIENT_QUANTITY_FORMAT).format(
                        (originalQuantity / initialPortions) * sliderPosition
                    )
                } ?: ingredient.quantity)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ScreenHeader(
            title = recipe.title,
            imageUrl = recipe.imageUrl
        )

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Dimens.paddingLarge)
        ) {
            IngredientsBlock(
                ingredients = calculatedIngredients,
                portionsCount = sliderPosition.toInt(),
                sliderPosition = sliderPosition,
                onSliderChange = { newPosition -> sliderPosition = newPosition }
            )

            CookingMethodBlock(
                steps = recipe.method
            )
        }
    }
}

@Composable
fun IngredientsBlock(
    ingredients: List<IngredientUiModel>,
    portionsCount: Int,
    sliderPosition: Float,
    onSliderChange: (Float) -> Unit
) {
    Text(
        text = stringResource(R.string.title_ingredients).uppercase(),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.displayLarge,
        modifier = Modifier.padding(top = Dimens.paddingLarge, bottom = Dimens.paddingExtraSmall)
    )

    PortionsSelector(
        portionsCount = portionsCount,
        sliderPosition = sliderPosition,
        onValueChange = onSliderChange
    )

    Column(
        modifier = Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PortionsSelector(
    portionsCount: Int,
    sliderPosition: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isDragged by interactionSource.collectIsDraggedAsState()
    val thumbScale by animateFloatAsState(
        targetValue =
            if (isDragged) UIConstants.SLIDER_THUMB_SCALE_DRAGGED
            else UIConstants.SLIDER_THUMB_SCALE_DEFAULT,
    )

    Column(modifier = modifier) {
        Text(
            text = stringResource(
                id = R.string.portions_count,
                portionsCount
            ),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondary,
        )

        Slider(
            value = sliderPosition,
            onValueChange = onValueChange,
            valueRange = UIConstants.MIN_PORTIONS..UIConstants.MAX_PORTIONS,
            steps = UIConstants.PORTIONS_SLIDER_STEPS,
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
fun CookingMethodBlock(steps: List<String>) {
    Text(
        text = stringResource(R.string.title_cooking_method).uppercase(),
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.displayLarge,
        modifier = Modifier.padding(vertical = Dimens.paddingLarge)
    )

    if (steps.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(Dimens.cornerRadiusMedium))
                .background(MaterialTheme.colorScheme.surface)
                .padding(Dimens.paddingBig)
        ) {
            steps.forEachIndexed { index, stepDescription ->
                MethodStep(description = stepDescription)

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

@Composable
fun IngredientItem(ingredient: IngredientUiModel) {
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

        val quantityInt = ingredient.quantity.toFloatOrNull()?.toInt()

        val unitText = if (quantityInt != null) {
            when (ingredient.unitOfMeasure) {
                UIConstants.UNIT_TABLESPOON_ABBREVIATION -> pluralStringResource(
                    R.plurals.unit_tablespoon,
                    quantityInt
                )

                UIConstants.UNIT_CLOVE_ABBREVIATION -> pluralStringResource(
                    R.plurals.unit_clove,
                    quantityInt
                )

                UIConstants.UNIT_PIECE_ABBREVIATION -> pluralStringResource(
                    R.plurals.unit_piece,
                    quantityInt
                )

                else -> ingredient.unitOfMeasure
            }
        } else {
            ingredient.unitOfMeasure
        }

        Text(
            text = "${ingredient.quantity} $unitText".uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(start = Dimens.paddingSmall)
        )
    }
}

@Composable
fun MethodStep(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSecondary,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview(showBackground = true, name = "Full Length Screen", heightDp = 1200)
@Composable
fun RecipeDetailsScreenPreview() {
    RecipesAppTheme {
        RecipeDetailsScreen(recipeId = 0)
    }
}

@Preview(showBackground = true, name = "Ingredients Block")
@Composable
fun IngredientsBlockPreview() {
    val ingredients =
        RecipesRepositoryStub.getRecipeById(0)?.toDomainModel()
            ?.toRecipeDetailUiModel()?.ingredients ?: emptyList()
    RecipesAppTheme {
        Column(modifier = Modifier.padding(Dimens.paddingLarge)) {
            IngredientsBlock(
                ingredients = ingredients,
                portionsCount = 4,
                sliderPosition = 4f,
                onSliderChange = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Cooking Method Block")
@Composable
fun CookingMethodBlockPreview() {
    val steps = RecipesRepositoryStub.getRecipeById(0)?.toDomainModel()?.method ?: emptyList()
    RecipesAppTheme {
        Column(modifier = Modifier.padding(Dimens.paddingLarge)) {
            CookingMethodBlock(steps = steps)
        }
    }
}
