package com.padabajka.dating.feature.infra
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class FrameStorage {

    private val buffers = mutableMapOf<String, Buffer>()
    private val globalMutex = Mutex()

    suspend fun recordFrame(screenName: String, frameTimeMs: Long) {
        val buffer = getOrCreate(screenName)
        buffer.add(frameTimeMs)
    }

    suspend fun drain(screenName: String): List<Long> {
        val buffer = getOrCreate(screenName)
        return buffer.drain()
    }

    suspend fun snapshot(screenName: String): List<Long> {
        val buffer = getOrCreate(screenName)
        return buffer.snapshot()
    }

    private suspend fun getOrCreate(screenName: String): Buffer {
        globalMutex.withLock {
            return buffers.getOrPut(screenName) {
                Buffer()
            }
        }
    }

    private class Buffer {

        private val mutex = Mutex()
        private val queue = mutableListOf<Long>()

        suspend fun add(value: Long) {
            mutex.withLock {
                queue.add(value)
            }
        }

        suspend fun drain(): List<Long> {
            mutex.withLock {
                if (queue.isEmpty()) return emptyList()

                val result = queue.toList()
                queue.clear()
                return result
            }
        }

        suspend fun snapshot(): List<Long> {
            mutex.withLock {
                return queue.toList()
            }
        }
    }
}
