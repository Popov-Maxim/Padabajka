package com.padabajka.dating.core.presentation.event

import androidx.compose.runtime.Immutable

@Immutable
sealed interface StateEvent {

    @Immutable
    data object Consumed : StateEvent

    @Immutable
    data object Raised : StateEvent
}

val consumed = StateEvent.Consumed
val raised = StateEvent.Raised

fun raisedIfNotNull(content: Any?): StateEvent = if (content != null) {
    raised
} else {
    consumed
}

@Immutable
sealed interface StateEventWithContent<out T> {

    @Immutable
    data object ConsumedWithContent : StateEventWithContent<Nothing>

    @Immutable
    data class RaisedWithContent<T>(val content: T) : StateEventWithContent<T>
}

fun consumed() = StateEventWithContent.ConsumedWithContent
fun <T> raised(content: T) = StateEventWithContent.RaisedWithContent(content)

fun <T> raisedWithContentIfNotNull(content: T?): StateEventWithContent<T> = if (content != null) {
    raised(content)
} else {
    consumed()
}
