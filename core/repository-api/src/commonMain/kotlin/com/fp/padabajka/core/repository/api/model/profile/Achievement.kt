package com.fp.padabajka.core.repository.api.model.profile

data class Achievement(
    val hidden: Boolean,
    val name: String,
    val description: String,
    val icon: Image
)
