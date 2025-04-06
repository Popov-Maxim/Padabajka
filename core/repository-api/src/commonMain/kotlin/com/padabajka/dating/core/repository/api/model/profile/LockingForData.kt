package com.padabajka.dating.core.repository.api.model.profile

data class LockingForData(
    val type: Text,
    val detail: Text? = null
) {
    companion object {
        val default = LockingForData(type = Text(Text.Id("just_meeting")))
    }
}
