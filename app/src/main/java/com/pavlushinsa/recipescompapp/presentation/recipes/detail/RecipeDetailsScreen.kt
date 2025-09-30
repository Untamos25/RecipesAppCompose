package com.pavlushinsa.recipescompapp.presentation.recipes.detail

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.pavlushinsa.recipescompapp.R
import com.pavlushinsa.recipescompapp.presentation.common.components.ScreenContentWrapper
import com.pavlushinsa.recipescompapp.presentation.common.components.ScreenHeader
import com.pavlushinsa.recipescompapp.presentation.common.effects.TopBarVisibilityEffect
import com.pavlushinsa.recipescompapp.presentation.common.theme.Dimens
import com.pavlushinsa.recipescompapp.presentation.common.theme.RecipesAppTheme
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.PreviewData.loadingState
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.PreviewData.successState
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.components.CookingMethodBlock
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.components.IngredientsList
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.components.PortionsSelector
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.model.IngredientUiModel
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.model.RecipeDetailsUiModel
import com.pavlushinsa.recipescompapp.presentation.recipes.detail.model.RecipeDetailsUiState
import kotlinx.collections.immutable.persistentListOf

private const val PRESSED_SCALE = 1.3f
private const val DEFAULT_SCALE = 1f
private const val POST_CLICK_SHRINK_SCALE = 0.9f
private const val POST_CLICK_ANIMATION_DURATION_MS = 100
private const val FAVORITE_ICON_CROSSFADE_DURATION_MS = 300

@Composable
fun RecipeDetailsScreen(
    viewModel: RecipeDetailViewModel,
    onTitleChanged: (String) -> Unit,
    onShowTopBarChanged: (Boolean) -> Unit
) {
    val recipeDetailsUiState by viewModel.recipeDetailsUiState.collectAsState()

    val recipeTitle = recipeDetailsUiState.recipe?.title

    LaunchedEffect(recipeTitle) {
        if (!recipeTitle.isNullOrBlank()) {
            onTitleChanged(recipeTitle)
        }
    }

    RecipeDetailsContent(
        recipeDetailsUiState = recipeDetailsUiState,
        onSliderChange = viewModel::onPortionsChange,
        onFavoriteClick = viewModel::onFavoriteClick,
        onRefresh = viewModel::onRefresh,
        onShowTopBarChanged = onShowTopBarChanged
    )
}

@Composable
private fun RecipeDetailsContent(
    recipeDetailsUiState: RecipeDetailsUiState,
    onSliderChange: (Float) -> Unit,
    onFavoriteClick: () -> Unit,
    onRefresh: () -> Unit,
    onShowTopBarChanged: (Boolean) -> Unit
) {
    ScreenContentWrapper(
        isLoading = recipeDetailsUiState.isLoading,
        isRefreshing = recipeDetailsUiState.isRefreshing,
        onRefresh = onRefresh
    ) {
        val recipe = recipeDetailsUiState.recipe
        if (recipe != null) {
            RecipeDetailsSuccessContent(
                recipe = recipe,
                portionsCount = recipeDetailsUiState.portionsCount,
                onSliderChange = onSliderChange,
                onFavoriteClick = onFavoriteClick,
                onShowTopBarChanged = onShowTopBarChanged
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.title_recipe_not_found))
            }
        }
    }
}

@Composable
private fun RecipeDetailsSuccessContent(
    recipe: RecipeDetailsUiModel,
    portionsCount: Float,
    onSliderChange: (Float) -> Unit,
    onFavoriteClick: () -> Unit,
    onShowTopBarChanged: (Boolean) -> Unit
) {
    val lazyListState = rememberLazyListState()
    TopBarVisibilityEffect(lazyListState, onShowTopBarChanged)

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

    LaunchedEffect(recipe.isFavorite) {
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

                Crossfade(
                    targetState = recipe.isFavorite,
                    animationSpec = tween(durationMillis = FAVORITE_ICON_CROSSFADE_DURATION_MS),
                    modifier = Modifier
                        .align(alignment = Alignment.TopEnd)
                        .padding(Dimens.paddingLarge)
                        .scale(scale.value)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            onClick = onFavoriteClick
                        ),
                    label = "FavoriteIconCrossfade"
                ) { isFavorite ->
                    val heartIcon =
                        if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
                    val contentDescriptionRes = if (isFavorite) {
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
            onRefresh = {},
            onShowTopBarChanged = {}
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
