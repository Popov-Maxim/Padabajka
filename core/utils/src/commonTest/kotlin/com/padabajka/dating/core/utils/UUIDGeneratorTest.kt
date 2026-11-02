package com.padabajka.dating.core.utils

import kotlin.test.Test
import kotlin.test.assertEquals

class UUIDGeneratorTest {
    @Test
    fun uuidFromKeyMatchesJvmGeneratedValues() {
        // Expected values were generated with JVM UUID.nameUUIDFromBytes and UTF-8.
        val cases = arrayOf(
            "a" to "0cc175b9-c0f1-36a8-b1c3-99e269772661",
            "user-123" to "bc17c97c-3d89-3d43-b7e0-7ca0266eafa8",
            "USER-123" to "8329405d-781f-3217-9951-42269e7d89d2",
            "0GZPqYpQwKfZf4vKX4De5oM3Hjc2" to "f35f955e-0184-35a3-9852-b3e029a4f57b",
            "01a09511-d641-4af5-aa5f-c2eb5e365249" to "99994092-9271-3429-bdb4-45aa0458ac17",
            "пользователь" to "1075de89-7df4-3cd7-a107-c5e32827ef92",
            "你好" to "7eca689f-0d33-39d9-9ea6-6ae112e5cfd7",
            "user-😀" to "8c9562e0-1c28-3f0a-90f7-40cb1badd60b",
            "é" to "66ddcd97-cfde-3bb2-b6fb-8a999b4bc76f",
            "e\u0301" to "5526861f-bb1e-31a1-bda6-ac364310a807",
            " user-123" to "6627cf06-09a6-344e-ba4b-861f11bd227f",
            "user-123 " to "3d6fa942-381c-3b72-88a7-9f093cbe6c70",
            "user\u0000id" to "77794182-431b-35e6-b314-1f5bb8cc6356",
            "a".repeat(128) to "e510683b-3f5f-3e40-93d0-21808bc6ff70",
        )

        cases.forEachIndexed { index, (key, expectedUuid) ->
            assertEquals(expectedUuid, uuidFromKey(key), "UUID mismatch for case $index")
        }
    }
}
