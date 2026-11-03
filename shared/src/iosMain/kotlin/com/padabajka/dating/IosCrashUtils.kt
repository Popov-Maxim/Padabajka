package com.padabajka.dating

import com.padabajka.dating.core.utils.printlnDev
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.crashlytics.crashlytics
import io.ktor.client.utils.unwrapCancellationException
import kotlinx.cinterop.ExperimentalForeignApi
import kotlin.experimental.ExperimentalNativeApi

object IosCrashUtils {
    @OptIn(ExperimentalNativeApi::class, ExperimentalForeignApi::class)
    fun installCrashReporter() {
        setUnhandledExceptionHook { throwable ->
            val exception = throwable.unwrapCancellationException()
            val stacktrace = exception.stackTraceToString()

            printlnDev("Fatal crash reason: $stacktrace")
            val log = "crashlog | stacktrace=$stacktrace"
            Firebase.crashlytics.log(log)
            terminateWithUnhandledException(exception)
        }
    }
}
