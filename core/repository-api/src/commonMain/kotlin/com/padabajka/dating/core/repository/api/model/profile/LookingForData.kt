package com.padabajka.dating.core.repository.api.model.profile

data class LookingForData(
    val type: Text,
    val detail: Text? = null
) {
    companion object {
        val default = LookingForData(type = Text(Text.Id("non_romantic")))
    }
}
