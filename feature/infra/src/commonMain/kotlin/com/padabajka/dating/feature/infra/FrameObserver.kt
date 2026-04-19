package com.padabajka.dating.feature.infra

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FrameObserver {
    private val _frames = MutableSharedFlow<Long>()
    val frames = _frames.asSharedFlow()

    suspend fun onFrame(frameTimeMs: Long) {
        _frames.emit(frameTimeMs)
    }
}
