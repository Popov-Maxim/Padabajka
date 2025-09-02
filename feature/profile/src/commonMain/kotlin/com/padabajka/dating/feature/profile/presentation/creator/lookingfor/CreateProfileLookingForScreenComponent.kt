package com.padabajka.dating.feature.profile.presentation.creator.lookingfor

import com.arkivanov.decompose.ComponentContext
import com.padabajka.dating.core.presentation.BaseComponent
import com.padabajka.dating.core.presentation.ui.dictionary.StaticTextId
import com.padabajka.dating.core.presentation.ui.dictionary.toText
import com.padabajka.dating.core.repository.api.model.profile.LookingForData
import com.padabajka.dating.core.repository.api.model.profile.Text
import com.padabajka.dating.feature.profile.domain.update.UpdateLookingForUseCase
import com.padabajka.dating.feature.profile.presentation.creator.lookingfor.model.CreateProfileLookingForEvent
import com.padabajka.dating.feature.profile.presentation.creator.lookingfor.model.CreateProfileLookingForState

class CreateProfileLookingForScreenComponent(
    context: ComponentContext,
    private val selectedType: StaticTextId,
    private val updateLookingForUseCase: UpdateLookingForUseCase,
    private val toNext: () -> Unit
) : BaseComponent<CreateProfileLookingForState>(
    context,
    CreateProfileLookingForState(selectedType)
) {
    fun onEvent(event: CreateProfileLookingForEvent) {
        when (event) {
            is CreateProfileLookingForEvent.OnDetailSelected -> updateLookingFor(event.detail)
        }
    }

    private fun updateLookingFor(detail: StaticTextId) {
        val lookingFor = LookingForData(
            type = selectedType.toText(type = Text.Type.Default),
            detail = detail.toText(type = Text.Type.Default)
        )
        mapAndReduceException(
            action = {
                updateLookingForUseCase(lookingFor)
                toNext()
            },
            mapper = { it },
            update = { state, _ -> state }
        )
    }
}
