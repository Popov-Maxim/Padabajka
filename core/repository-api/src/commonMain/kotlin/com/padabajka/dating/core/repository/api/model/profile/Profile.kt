package com.padabajka.dating.core.repository.api.model.profile

import kotlinx.collections.immutable.PersistentList
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.todayIn

data class Profile(
    val name: String,
    val birthday: LocalDate,
    val images: PersistentList<Image>,
    val aboutMe: String,
    val details: PersistentList<Detail>,
    val mainAchievement: Achievement?,
    val achievements: PersistentList<Achievement>
) {
    val age: Age
        get() {
            val timeZone = TimeZone.currentSystemDefault()
            val now = Clock.System.todayIn(timeZone)

            return birthday.periodUntil(now).years.toAge()
        }
} // TODO: add override equals()
