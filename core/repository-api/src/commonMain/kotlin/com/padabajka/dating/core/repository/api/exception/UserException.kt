package com.padabajka.dating.core.repository.api.exception

object UserException {
    class Deleted : Exception("User was deleted")
    class Banned : Exception("User was banned")
}
