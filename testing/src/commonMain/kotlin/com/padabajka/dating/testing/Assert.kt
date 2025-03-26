package com.padabajka.dating.testing

import kotlin.test.assertEquals
import kotlin.test.fail

@Suppress("TooGenericExceptionCaught")
inline fun assertThrows(exception: Throwable, action: () -> Unit) {
    try {
        action()
        fail("Exception was expected!")
    } catch (ae: AssertionError) {
        throw ae
    } catch (e: Throwable) {
        assertEquals(exception, e)
    }
}
