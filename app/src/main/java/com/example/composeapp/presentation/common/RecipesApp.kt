package com.example.composeapp.presentation.common

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.composeapp.R
import com.example.composeapp.presentation.common.components.BottomNavigation
import com.example.composeapp.presentation.common.mapper.toStringResId
import com.example.composeapp.presentation.common.model.UiEvent
import com.example.composeapp.presentation.common.navigation.AppNavigation
import com.example.composeapp.presentation.common.navigation.Destination
import com.example.composeapp.presentation.common.theme.RecipesAppTheme
import kotlinx.coroutines.launch

@Composable
fun RecipesApp() {
    RecipesAppTheme {
        val navController = rememberNavController()
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val appViewModel: AppViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            appViewModel.eventFlow.collect { event ->
                when (event) {
                    is UiEvent.ShowSnackBarEvent -> {
                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = context.getString(event.errorType.toStringResId()),
                                actionLabel = if (event.errorType.isRetryable) {
                                    context.getString(R.string.action_retry)
                                } else null
                            )
                            if (result == SnackbarResult.ActionPerformed) {
                                event.onRetry?.invoke()
                            }
                        }
                    }
                }
            }
        }


        RecipesAppContent(
            snackbarHostState = snackbarHostState,
            onCategoriesClick = { navController.navigate(Destination.Categories.route) },
            onFavoriteClick = { navController.navigate(Destination.Favorites.route) },
            content = { modifier ->
                AppNavigation(
                    navController = navController,
                    modifier = modifier
                )
            }
        )
    }
}

@Composable
private fun RecipesAppContent(
    snackbarHostState: SnackbarHostState,
    onCategoriesClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    content: @Composable (modifier: Modifier) -> Unit,
) {
    RecipesAppTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                BottomNavigation(
                    onCategoriesClick = onCategoriesClick,
                    onFavoriteClick = onFavoriteClick
                )
            }
        ) { innerPadding ->
            content(Modifier.padding(innerPadding))
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, locale = "en", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RecipesAppContentPreview() {
    RecipesAppTheme {
        RecipesAppContent(
            snackbarHostState = remember { SnackbarHostState() },
            onCategoriesClick = {},
            onFavoriteClick = {},
            content = { modifier ->
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .border(width = 2.dp, color = Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    Text("NavHost")
                }
            }
        )
    }
}
