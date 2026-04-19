package com.padabajka.dating.core.presentation

import com.padabajka.dating.feature.infra.FrameMetrics
import com.padabajka.dating.feature.infra.FrameMetricsAggregator
import com.padabajka.dating.feature.infra.FrameObserver
import com.padabajka.dating.feature.infra.FrameStorage
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.perf.FirebasePerformance
import dev.gitlive.firebase.perf.metrics.Trace
import dev.gitlive.firebase.perf.performance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToLong

class ComponentLifecycleListener(
    screenName: String,
    private val frameObserver: FrameObserver,
    private val frameStorage: FrameStorage,
    private val coroutineScope: CoroutineScope,
    private val frameMetricsAggregator: FrameMetricsAggregator,
    private val firebasePerf: FirebasePerformance = Firebase.performance
) {

    private val traceName = "screen_render_$screenName"

    private var frameJob: Job? = null
    private var trace: Trace? = null

    fun onResume() {
        coroutineScope.launch {
            trace = firebasePerf.newTrace(traceName).apply { start() }
            frameJob = frameObserver.frames.collect {
                frameStorage.recordFrame(traceName, it)
            }
        }
    }

    fun onPause() {
        coroutineScope.launch {
            frameJob?.cancel()

            val frameMetrics = frameMetricsAggregator.aggregate(traceName)
            if (frameMetrics != null) {
                trace?.setFrameMetrics(frameMetrics)
            }
            trace?.stop()
            trace = null
        }
    }
}

private fun Trace.setFrameMetrics(frameMetrics: FrameMetrics) {
    putMetric("frame_count", frameMetrics.frameCount.toLong())
    putMetric("duration_ms", frameMetrics.durationMs)
    putMetric("avg_fps_x10", (frameMetrics.avgFps * x10).roundToLong())
    putMetric("avg_frame_time_ms", frameMetrics.avgFrameTimeMs.roundToLong())
    putMetric("min_frame_time_ms", frameMetrics.minFrameTimeMs)
    putMetric("max_frame_time_ms", frameMetrics.maxFrameTimeMs)
    putMetric("p50_frame_time_ms", frameMetrics.p50FrameTimeMs)
    putMetric("p90_frame_time_ms", frameMetrics.p90FrameTimeMs)
    putMetric("p95_frame_time_ms", frameMetrics.p95FrameTimeMs)
    putMetric("slow_frames_percent_x10", (frameMetrics.slowFramesPercent * x10).roundToLong())
    putMetric("frozen_frames_percent_x10", (frameMetrics.frozenFramesPercent * x10).roundToLong())
}

@Suppress("TopLevelPropertyNaming")
private const val x10 = 10
