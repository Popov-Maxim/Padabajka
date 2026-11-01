package com.padabajka.dating.core.networking.utils

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import io.ktor.util.AttributeKey

private val locallyHandledStatuses = AttributeKey<Set<HttpStatusCode>>("LocallyHandledResponseStatuses")

/** Leaves validation of these statuses to the caller reading the response. */
fun HttpRequestBuilder.handleResponseStatuses(vararg statuses: HttpStatusCode) {
    attributes.put(locallyHandledStatuses, statuses.toSet())
}

internal fun HttpResponse.isStatusHandledLocally(): Boolean =
    status in request.attributes.getOrNull(locallyHandledStatuses).orEmpty()
