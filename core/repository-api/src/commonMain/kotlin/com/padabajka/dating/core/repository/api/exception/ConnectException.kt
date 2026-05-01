package com.padabajka.dating.core.repository.api.exception

class ConnectException(cause: Throwable) : Exception(cause)

class BadStatusCodeException(val statusCode: Int, description: String) : Exception("$statusCode: $description")
