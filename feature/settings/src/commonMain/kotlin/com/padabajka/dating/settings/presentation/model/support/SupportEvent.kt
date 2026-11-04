package com.padabajka.dating.settings.presentation.model.support

sealed interface SupportEvent {
    data object NavigateBack : SupportEvent
    data class SelectCategory(val category: SupportCategory) : SupportEvent
    data class UpdateDescription(val description: String) : SupportEvent
    data object OpenMail : SupportEvent
}
