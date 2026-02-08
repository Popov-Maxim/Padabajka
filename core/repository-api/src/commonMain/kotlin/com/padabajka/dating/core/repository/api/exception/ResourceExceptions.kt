package com.padabajka.dating.core.repository.api.exception

object ResourceExceptions {
    class Deleted(resourceName: String, id: String) : Exception("Resource $resourceName(id#$id) deleted")
}
