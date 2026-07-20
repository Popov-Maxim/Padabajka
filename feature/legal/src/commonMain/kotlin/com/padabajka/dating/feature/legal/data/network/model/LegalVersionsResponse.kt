package com.padabajka.dating.feature.legal.data.network.model

import com.padabajka.dating.core.repository.api.model.legal.LegalVersions
import kotlinx.serialization.Serializable

@Serializable
data class LegalVersionsResponse(
    val privacy: String?,
    val termsOfUse: String?,
)

fun LegalVersionsResponse.toDomain(): LegalVersions {
    return LegalVersions(
        privacy = privacy ?: "", // TODO(P0)
        terms = termsOfUse ?: ""
    )
}
