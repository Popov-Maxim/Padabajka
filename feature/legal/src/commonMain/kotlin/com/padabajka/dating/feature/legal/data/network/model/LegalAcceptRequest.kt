package com.padabajka.dating.feature.legal.data.network.model

import com.padabajka.dating.core.repository.api.model.legal.LegalVersions
import kotlinx.serialization.Serializable

@Serializable
data class LegalAcceptRequest(
    val privacy: String,
    val termsOfUse: String,
)

fun LegalVersions.toRequest(): LegalAcceptRequest {
    return LegalAcceptRequest(
        privacy = privacy,
        termsOfUse = terms,
    )
}
