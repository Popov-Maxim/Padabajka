package com.padabajka.dating.feature.profile.domain.creator

class NameValidator {
    fun validate(name: String): Boolean {
        return name.length in validRangeLength &&
            name.first().isWhitespace().not() &&
            name.last().isWhitespace().not() &&
            name.isNotBlank()
    }

    companion object {
        private val validRangeLength = 3..50
    }
}
