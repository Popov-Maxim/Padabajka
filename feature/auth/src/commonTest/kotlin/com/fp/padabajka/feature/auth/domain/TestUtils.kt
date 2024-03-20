package com.fp.padabajka.feature.auth.domain

import org.kodein.mock.Mocker
import kotlin.test.assertEquals
import kotlin.test.fail

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

infix fun Mocker.EverySuspend<*>.throws(e: Throwable) = runs { throw e }
