package com.padabajka.dating.feature.profile.presentation.creator.birthday

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.repository.api.model.profile.fromMillis
import com.padabajka.dating.feature.profile.domain.creator.BirthdayValidator
import com.padabajka.dating.feature.profile.domain.creator.DraftProfileProvider
import com.padabajka.dating.feature.profile.domain.update.BirthdayUpdateUseCase
import com.padabajka.dating.feature.profile.presentation.creator.birthday.model.BirthdayItem
import com.padabajka.dating.feature.profile.presentation.creator.birthday.model.CreateProfileBirthdayEvent
import com.padabajka.dating.feature.profile.presentation.creator.birthday.model.CreateProfileBirthdayState
import com.padabajka.dating.feature.profile.presentation.editor.model.Issue
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileField
import kotlinx.datetime.LocalDate

class CreateProfileBirthdayScreenComponent(
    context: ComponentContext,
    private val birthdayValidator: BirthdayValidator,
    private val birthdayUpdateUseCase: BirthdayUpdateUseCase,
    draftProfileProvider: DraftProfileProvider,
    private val toNext: () -> Unit
) : BaseComponent<CreateProfileBirthdayState>(
    context,
    initState(draftProfileProvider)
) {

    fun onEvent(event: CreateProfileBirthdayEvent) {
        when (event) {
            is CreateProfileBirthdayEvent.BirthdayFieldUpdate -> updateBirthday(event.dateMillis)
            CreateProfileBirthdayEvent.PressContinue -> continueCreating()
        }
    }

    private fun updateBirthday(dateMillis: Long) {
        val birthday = LocalDate.fromMillis(dateMillis)
        val validResult = birthdayValidator.validate(birthday)
        val issueText = when (validResult) {
            BirthdayValidator.Result.Valid -> null
            BirthdayValidator.Result.TooYoung -> StaticTextId.UiId.MessageForTooYoung
            BirthdayValidator.Result.TooOld -> StaticTextId.UiId.MessageForTooOld
        }
        val issue = issueText?.run { Issue.Text(this) }
        reduce {
            val birthdayItem = BirthdayItem(birthday)
            it.copy(
                birthdayField = it.birthdayField
                    .updatedValue(birthdayItem)
                    .updatedIssues { issues ->
                        if (issue != null) issues + (birthdayItem to issue) else issues
                    }
            )
        }
    }

    private fun continueCreating() {
        val birthdayField = state.value.birthdayField
        val birthdayItem = birthdayField.value
        val issue = birthdayField.currentIssue
        if (birthdayItem == null || issue != null) return

        mapAndReduceException(
            action = {
                birthdayUpdateUseCase(birthdayItem.date)
                toNext()
            },
            mapper = { it },
            update = { state, _ -> state }
        )
    }

    companion object {
        private fun initState(draftProfileProvider: DraftProfileProvider): CreateProfileBirthdayState {
            val draftProfile = draftProfileProvider.getProfile()
            val birthday = draftProfile?.birthday
            val birthdayItem = birthday?.run {
                BirthdayItem(birthday)
            }
            val birthdayField = ProfileField(birthdayItem)

            return CreateProfileBirthdayState(birthdayField)
        }
    }
}
