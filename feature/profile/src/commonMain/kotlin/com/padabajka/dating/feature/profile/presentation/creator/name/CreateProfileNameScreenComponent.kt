package com.padabajka.dating.feature.profile.presentation.creator.name

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.feature.profile.domain.creator.DraftProfileProvider
import com.padabajka.dating.feature.profile.domain.creator.NameValidator
import com.padabajka.dating.feature.profile.domain.update.FirstNameUpdateUseCase
import com.padabajka.dating.feature.profile.presentation.creator.name.model.CreateProfileNameEvent
import com.padabajka.dating.feature.profile.presentation.creator.name.model.CreateProfileNameState
import com.padabajka.dating.feature.profile.presentation.editor.model.Issue
import com.padabajka.dating.feature.profile.presentation.editor.model.ProfileField

class CreateProfileNameScreenComponent(
    context: ComponentContext,
    private val nameValidator: NameValidator,
    private val saveNameUseCase: FirstNameUpdateUseCase,
    draftProfileProvider: DraftProfileProvider,
    private val toNext: () -> Unit
) : BaseComponent<CreateProfileNameState>(
    context,
    initState(draftProfileProvider)
) {

    fun onEvent(event: CreateProfileNameEvent) {
        when (event) {
            is CreateProfileNameEvent.NameFieldUpdate -> updateName(event.name)
            CreateProfileNameEvent.PressContinue -> continueCreating()
        }
    }

    private fun updateName(name: String) {
        reduce {
            it.copy(nameField = it.nameField.updatedValue(name))
        }
    }

    private fun continueCreating() {
        val name = state.value.nameField.value
        val valid = nameValidator.validate(name)

        if (valid) {
            mapAndReduceException(
                action = {
                    saveNameUseCase(name)
                    toNext()
                },
                mapper = { it },
                update = { state, _ -> state }
            )
        } else {
            reduce { state ->
                val issue = Issue("error") // TODO: add issue
                state.copy(nameField = state.nameField.updatedIssues { it + (name to issue) })
            }
        }
    }

    companion object {
        private fun initState(draftProfileProvider: DraftProfileProvider): CreateProfileNameState {
            val draftProfile = draftProfileProvider.getProfile()
            val nameField = ProfileField(draftProfile?.name ?: "")

            return CreateProfileNameState(nameField)
        }
    }
}
