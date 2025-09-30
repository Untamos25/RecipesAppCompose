package com.pavlushinsa.recipescompapp.presentation.common

import com.pavlushinsa.recipescompapp.presentation.common.model.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

interface AppWideEventDelegate {
    fun sendAppWideEvent(event: UiEvent)
}

@Singleton
class AppWideEventHandler @Inject constructor() : AppWideEventDelegate {
    private val eventChannel = Channel<UiEvent>(Channel.BUFFERED)
    val eventFlow = eventChannel.receiveAsFlow()

    override fun sendAppWideEvent(event: UiEvent) {
        eventChannel.trySend(event)
    }
}