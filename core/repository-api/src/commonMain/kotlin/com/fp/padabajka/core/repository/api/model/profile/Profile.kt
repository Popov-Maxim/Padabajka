package com.fp.padabajka.core.repository.api.model.profile

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.todayIn

data class Profile(
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate,
    val images: List<Image>,
    val aboutMe: String,
    val details: List<Detail>,
    val mainAchievement: Achievement?,
    val achievements: List<Achievement>
) {
    val age: Int
        get() {
            val timeZone = TimeZone.currentSystemDefault()
            val now = Clock.System.todayIn(timeZone)

            return now.periodUntil(birthday).years
        }
} // TODO: add override equals()
