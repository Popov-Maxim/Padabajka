package com.padabajka.dating.core.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.padabajka.dating.core.domain.MutableAppSettings
import org.koin.compose.koinInject
import kotlin.math.min

@Composable
fun FpsMonitor(
    modifier: Modifier = Modifier,
    updateIntervalMs: Int = 1000,
    updateMinFpsMs: Int = 10000,
) {
    val settings: MutableAppSettings = koinInject()
    val showFps by settings.showFps.collectAsState()
    if (showFps.not()) return

    var fps: Int? by remember { mutableStateOf(null) }
    var minFps: Int? by remember { mutableStateOf(null) }
    var cFps: Int? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        var frameCount = 0
        var frameTime: Long? = null
        var minFpsframeTime: Long? = null
        var lastTime: Long? = null
        while (true) {
            withFrameMillis { millis ->
                lastTime = lastTime?.let {
                    cFps = SECOND_IN_MILLIS / (millis - it).toInt()
                    millis
                } ?: millis

                frameTime = frameTime ?: millis
                minFpsframeTime = minFpsframeTime ?: millis
                if (millis > minFpsframeTime!! + updateMinFpsMs) {
                    minFps = null
                    minFpsframeTime = minFpsframeTime!! + updateMinFpsMs
                }
                if (millis > frameTime!! + updateIntervalMs) {
                    fps = frameCount * (SECOND_IN_MILLIS / updateIntervalMs)
                    minFps = minNotNull(minFps, fps)
                    frameTime = frameTime!! + updateIntervalMs
                    frameCount = 0
                }
                frameCount++
            }
        }
    }

    Box(
        modifier = modifier
            .background(Color.Black.copy(alpha = 1f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "FPS: $fps $minFps $cFps",
            color = Color.White,
        )
    }
}

private fun minNotNull(a: Int?, b: Int?): Int? {
    return if (a == null) b else if (b == null) a else min(a.toInt(), b.toInt())
}

private const val SECOND_IN_MILLIS = 1000
