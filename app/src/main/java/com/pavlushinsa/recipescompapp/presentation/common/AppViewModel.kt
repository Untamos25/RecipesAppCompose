package com.pavlushinsa.recipescompapp.presentation.common

import androidx.lifecycle.ViewModel
import com.pavlushinsa.recipescompapp.presentation.common.model.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appWideEventHandler: AppWideEventHandler
) : ViewModel(), AppWideEventDelegate by appWideEventHandler {

    val eventFlow: Flow<UiEvent> = appWideEventHandler.eventFlow
}
