package com.padabajka.dating.feature.infra

data class FrameMetrics(
    val frameCount: Int,
    val durationMs: Long,

    val avgFps: Double,
    val avgFrameTimeMs: Double,
    val minFrameTimeMs: Long,
    val maxFrameTimeMs: Long,
    val p50FrameTimeMs: Long,
    val p90FrameTimeMs: Long,
    val p95FrameTimeMs: Long,

    val slowFramesPercent: Double,
    val frozenFramesPercent: Double,
)
