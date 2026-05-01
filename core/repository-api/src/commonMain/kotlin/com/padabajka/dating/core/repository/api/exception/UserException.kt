package com.padabajka.dating.core.repository.api.exception

sealed interface UserException {
    class Deleted : Exception("User was deleted"), UserException
    class Banned : Exception("User was banned"), UserException
    class Unauthorized : Exception("User is unauthorized"), UserException
}
