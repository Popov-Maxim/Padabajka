package com.padabajka.dating.core.repository.api.model.profile

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.periodUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn

data class Profile(
    val name: String,
    val birthday: LocalDate,
    val images: List<Image>,
    val aboutMe: String,
    val lookingFor: LookingForData,
    val details: List<Detail>,
    val lifestyles: List<Lifestyle>,
    val mainAchievement: Achievement?,
    val achievements: List<Achievement>
) {
    val age: Age
        get() {
            val timeZone = TimeZone.currentSystemDefault()
            val now = Clock.System.todayIn(timeZone)

            return birthday.periodUntil(now).years.toAge()
        }
} // TODO: add override equals()

val LocalDate.age: Age
    get() {
        val timeZone = TimeZone.currentSystemDefault()
        val now = Clock.System.todayIn(timeZone)

        return this.periodUntil(now).years.toAge()
    }

fun LocalDate.Companion.fromMillis(millis: Long): LocalDate =
    Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.currentSystemDefault()).date

sealed interface ProfileState {
    data object Idle : ProfileState
    data object NotCreated : ProfileState
    data class Existing(val profile: Profile) : ProfileState
}
