package com.padabajka.dating.feature.legal.data.network.model

import com.padabajka.dating.core.repository.api.model.legal.UserLegalVersions
import kotlinx.serialization.Serializable

@Serializable
data class MeLegalVersionsResponse(
    val actual: LegalVersionsResponse,
    val accepted: LegalVersionsResponse,
    val needUpdate: Boolean
)

fun MeLegalVersionsResponse.toDomain(): UserLegalVersions {
    return UserLegalVersions(
        actual = actual.toDomain(),
        accepted = accepted.toDomain()
    )
}
