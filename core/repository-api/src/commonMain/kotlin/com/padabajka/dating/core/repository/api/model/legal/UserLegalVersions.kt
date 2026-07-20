package com.padabajka.dating.core.repository.api.model.legal

data class UserLegalVersions(
    val actual: LegalVersions,
    val accepted: LegalVersions
)
