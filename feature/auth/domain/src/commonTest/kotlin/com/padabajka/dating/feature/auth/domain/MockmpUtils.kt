package com.padabajka.dating.feature.auth.domain

import org.kodein.mock.Mocker

infix fun Mocker.EverySuspend<*>.throws(e: Throwable) = runs { throw e }
