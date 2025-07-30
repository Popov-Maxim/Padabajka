package com.padabajka.dating.core.repository.api.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class LookingForData(
    val type: Text,
    val detail: Text? = null
) {
    companion object {
        val default = LookingForData(
            type = Text(
                Text.Id("non_romantic"),
                Text.Type.UI
            )
        )
    }
}
