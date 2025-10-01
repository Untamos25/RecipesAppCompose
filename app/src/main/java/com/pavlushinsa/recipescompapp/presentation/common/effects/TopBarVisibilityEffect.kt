package com.pavlushinsa.recipescompapp.presentation.common.effects

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun TopBarVisibilityEffect(
    lazyListState: LazyListState,
    onShowTopBarChange: (Boolean) -> Unit
) {
    val showTopBar by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex > 0
        }
    }

    LaunchedEffect(showTopBar) {
        onShowTopBarChange(showTopBar)
    }
}

@Composable
fun TopBarVisibilityEffect(
    lazyGridState: LazyGridState,
    onShowTopBarChange: (Boolean) -> Unit
) {
    val showTopBar by remember {
        derivedStateOf {
            lazyGridState.firstVisibleItemIndex > 0
        }
    }

    LaunchedEffect(showTopBar) {
        onShowTopBarChange(showTopBar)
    }
}
