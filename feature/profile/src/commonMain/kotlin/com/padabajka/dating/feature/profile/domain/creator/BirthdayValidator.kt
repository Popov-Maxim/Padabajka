package com.padabajka.dating.feature.profile.domain.creator

import com.padabajka.dating.core.repository.api.model.profile.Age
import com.padabajka.dating.core.repository.api.model.profile.age
import com.padabajka.dating.core.repository.api.model.profile.compareTo
import kotlinx.datetime.LocalDate

class BirthdayValidator {
    fun validate(birthday: LocalDate): Result {
        return if (birthday.age < Age.minAge) {
            Result.TooYoung
        } else if (birthday.age > Age.maxAge) {
            Result.TooOld
        } else {
            Result.Valid
        }
    }

    sealed interface Result {
        data object Valid : Result
        data object TooYoung : Result
        data object TooOld : Result
    }
}
