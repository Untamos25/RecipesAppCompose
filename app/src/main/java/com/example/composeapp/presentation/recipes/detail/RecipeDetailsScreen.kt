package com.example.composeapp.presentation.recipes.detail

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.composeapp.R
import com.example.composeapp.presentation.common.components.CollapsingAppBar
import com.example.composeapp.presentation.common.components.ScreenHeader
import com.example.composeapp.presentation.common.constants.SliderConstants.MAX_PORTIONS
import com.example.composeapp.presentation.common.constants.SliderConstants.MIN_PORTIONS
import com.example.composeapp.presentation.common.constants.SliderConstants.PORTIONS_SLIDER_STEPS
import com.example.composeapp.presentation.common.constants.SliderConstants.SLIDER_THUMB_SCALE_DEFAULT
import com.example.composeapp.presentation.common.constants.SliderConstants.SLIDER_THUMB_SCALE_DRAGGED
import com.example.composeapp.presentation.common.theme.Dimens
import com.example.composeapp.presentation.common.theme.RecipesAppTheme
import com.example.composeapp.presentation.recipes.detail.PreviewData.loadingState
import com.example.composeapp.presentation.recipes.detail.PreviewData.successState
import com.example.composeapp.presentation.recipes.detail.formatter.IngredientFormatter
import com.example.composeapp.presentation.recipes.detail.formatter.QuantityFormatter
import com.example.composeapp.presentation.recipes.detail.model.IngredientUiModel
import com.example.composeapp.presentation.recipes.detail.model.RecipeDetailsUiModel
import com.example.composeapp.presentation.recipes.detail.model.RecipeDetailsUiState
import kotlinx.collections.immutable.persistentListOf

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailViewModel,
) {
    val recipeDetailsUiState by viewModel.recipeDetailsUiState.collectAsState()

    RecipeDetailsContent(
        recipeDetailsUiState = recipeDetailsUiState,
        onSliderChange = viewModel::onPortionsChange,
        onFavoriteClick = viewModel::onFavoriteClick,
        onRefresh = viewModel::onRefresh
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecipeDetailsContent(
    recipeDetailsUiState: RecipeDetailsUiState,
    onSliderChange: (Float) -> Unit,
    onFavoriteClick: () -> Unit,
    onRefresh: () -> Unit
) {
    val recipe = recipeDetailsUiState.recipe

    PullToRefreshBox(
        isRefreshing = recipeDetailsUiState.isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            recipeDetailsUiState.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            recipe == null -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.title_recipe_not_found))
                }
            }

            else -> {
                RecipeDetailsSuccessContent(
                    recipe = recipe,
                    portionsCount = recipeDetailsUiState.portionsCount,
                    onSliderChange = onSliderChange,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }
}

@Composable
private fun RecipeDetailsSuccessContent(
    recipe: RecipeDetailsUiModel,
    portionsCount: Float,
    onSliderChange: (Float) -> Unit,
    onFavoriteClick: () -> Unit
) {
    val lazyListState = rememberLazyListState()

    val showTopBar by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Box {
                    ScreenHeader(
                        title = recipe.title,
                        imageUrl = recipe.imageUrl,
                        contentDescription = recipe.title
                    )

                    val heartIcon =
                        if (recipe.isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
                    val heartIconContentDescription = if (recipe.isFavorite) {
                        stringResource(R.string.content_description_remove_from_favorites)
                    } else {
                        stringResource(R.string.content_description_add_to_favorites)
                    }

                    Image(
                        painter = painterResource(heartIcon),
                        contentDescription = heartIconContentDescription,
                        modifier = Modifier
                            .align(alignment = Alignment.TopEnd)
                            .padding(Dimens.paddingLarge)
                            .size(Dimens.iconSizeLarge)
                            .clickable(onClick = onFavoriteClick)
                    )
                }
            }

            item {
                Column(Modifier.padding(horizontal = Dimens.paddingLarge)) {
                    Text(
                        text = stringResource(R.string.title_ingredients).uppercase(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayLarge,
                        modifier = Modifier.padding(
                            top = Dimens.paddingLarge,
                            bottom = Dimens.paddingExtraSmall
                        )
                    )

                    PortionsSelector(
                        portionsCount = portionsCount,
                        onValueChange = onSliderChange
                    )
                }
            }

            item {
                IngredientsList(
                    ingredients = recipe.ingredients,
                    modifier = Modifier.padding(horizontal = Dimens.paddingLarge)
                )
            }

            item {
                Text(
                    text = stringResource(R.string.title_cooking_method).uppercase(),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.padding(
                        top = Dimens.paddingLarge,
                        bottom = Dimens.paddingLarge,
                        start = Dimens.paddingLarge,
                        end = Dimens.paddingLarge
                    )
                )
            }

            item {
                CookingMethodBlock(
                    steps = recipe.method,
                    modifier = Modifier.padding(horizontal = Dimens.paddingLarge)
                )
            }
        }

        AnimatedVisibility(
            visible = showTopBar,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CollapsingAppBar(title = recipe.title)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PortionsSelector(
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
private fun IngredientsList(
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
private fun IngredientItem(ingredient: IngredientUiModel) {
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
private fun CookingMethodBlock(
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

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "en", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RecipeDetailsContentPreview(
    @PreviewParameter(RecipeDetailsUiStateProvider::class) state: RecipeDetailsUiState
) {
    RecipesAppTheme {
        RecipeDetailsContent(
            recipeDetailsUiState = state,
            onSliderChange = {},
            onFavoriteClick = {},
            onRefresh = {}
        )
    }
}

private class RecipeDetailsUiStateProvider : PreviewParameterProvider<RecipeDetailsUiState> {
    override val values = sequenceOf(successState, loadingState)
}

private object PreviewData {
    private val previewRecipe = RecipeDetailsUiModel(
        id = 0,
        title = "Название рецепта",
        imageUrl = "",
        ingredients = persistentListOf(
            IngredientUiModel("4", "шт", "ингредиент 1"),
            IngredientUiModel("1", "кг", "ингредиент 2"),
            IngredientUiModel("1.5", "ст. ложка", "ингредиент 3")
        ),
        method = persistentListOf(
            "1. Первый шаг приготовления",
            "2. Второй шаг приготовления весьма продолжительный, что занимает несколько строк",
            "3. Третий шаг приготовления",
        ),
        isFavorite = true
    )

    val successState = RecipeDetailsUiState(
        recipe = previewRecipe,
        portionsCount = 3f,
        isLoading = false
    )

    val loadingState = RecipeDetailsUiState(
        isLoading = true
    )
}
