package com.fp.padabajka.feature.auth.domain

import kotlin.test.assertEquals
import kotlin.test.fail

fun assertThrows(exception: Throwable, action: () -> Unit) {
    try {
        action()
        fail("Exception was expected!")
    } catch (ae: AssertionError) {
        throw ae
    } catch (e: Throwable) {
        assertEquals(exception, e)
    }
}
