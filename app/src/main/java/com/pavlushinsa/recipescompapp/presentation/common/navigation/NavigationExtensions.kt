package com.pavlushinsa.recipescompapp.presentation.common.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private const val ANIMATION_DURATION = 300
private const val ANIMATION_OFFSET = 1000

fun NavGraphBuilder.animatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = { slideInHorizontally(initialOffsetX = { ANIMATION_OFFSET }, animationSpec = tween(ANIMATION_DURATION)) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -ANIMATION_OFFSET }, animationSpec = tween(ANIMATION_DURATION)) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -ANIMATION_OFFSET }, animationSpec = tween(ANIMATION_DURATION)) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { ANIMATION_OFFSET }, animationSpec = tween(ANIMATION_DURATION)) + fadeOut() },
        content = content
    )
}

fun NavGraphBuilder.bottomBarComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            if (targetState.destination.route == Destination.Categories.route) {
                slideInHorizontally(initialOffsetX = { -ANIMATION_OFFSET }, animationSpec = tween(ANIMATION_DURATION))
            } else {
                slideInHorizontally(initialOffsetX = { ANIMATION_OFFSET }, animationSpec = tween(ANIMATION_DURATION))
            } + fadeIn()
        },
        exitTransition = {
            if (targetState.destination.route == Destination.Categories.route) {
                slideOutHorizontally(targetOffsetX = { ANIMATION_OFFSET }, animationSpec = tween(ANIMATION_DURATION))
            } else {
                slideOutHorizontally(targetOffsetX = { -ANIMATION_OFFSET }, animationSpec = tween(ANIMATION_DURATION))
            } + fadeOut()
        },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -ANIMATION_OFFSET }, animationSpec = tween(ANIMATION_DURATION)) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { ANIMATION_OFFSET }, animationSpec = tween(ANIMATION_DURATION)) + fadeOut() },
        content = content
    )
}
