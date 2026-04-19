package com.padabajka.dating.feature.infra

class FrameMetricsAggregator(
    private val frameStorage: FrameStorage
) {
    suspend fun aggregate(screenName: String): FrameMetrics? {
        val frames = frameStorage.drain(screenName)
        return aggregate(frames)
    }

    suspend fun aggregateSnapshot(screenName: String): FrameMetrics? {
        val frames = frameStorage.snapshot(screenName)
        return aggregate(frames)
    }

    @Suppress("MagicNumber")
    private fun aggregate(frames: List<Long>): FrameMetrics? {
        if (frames.isEmpty()) return null

        val sorted = frames.sorted()

        val frameCount = sorted.size
        val durationMs = sorted.sum()

        val avg = durationMs.toDouble() / frameCount

        val min = sorted.first()
        val max = sorted.last()

        fun percentile(p: Double): Long {
            val index = ((frameCount - 1) * p).toInt()
            return sorted[index]
        }

        val p50 = percentile(0.50)
        val p90 = percentile(0.90)
        val p95 = percentile(0.95)

        val slowFramesPercentX100 = frames.count { it > 20 }.toDouble() * 100 / frameCount
        val frozenFramesPercentX100 = frames.count { it > 700 }.toDouble() * 100 / frameCount

        return FrameMetrics(
            frameCount = frameCount,
            durationMs = durationMs,

            avgFps = 1000 / avg,
            avgFrameTimeMs = avg,
            minFrameTimeMs = min,
            maxFrameTimeMs = max,
            p50FrameTimeMs = p50,
            p90FrameTimeMs = p90,
            p95FrameTimeMs = p95,

            slowFramesPercent = slowFramesPercentX100,
            frozenFramesPercent = frozenFramesPercentX100
        )
    }
}
