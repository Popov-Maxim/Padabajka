package com.padabajka.dating.core.repository.api.exception

sealed interface GeoExceptions {
    class HasNotPermission : IllegalStateException(), GeoExceptions
    class UnknownError : IllegalStateException(), GeoExceptions
}
