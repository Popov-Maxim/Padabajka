package com.padabajka.dating.settings.data.network

internal interface AuthMetadataApi {
    suspend fun patch(metadataDto: UpdateMetadataDto)
    suspend fun delete(metadataDto: DeleteMetadataDto)

    companion object {
        const val PATH = "metadata"
    }
}
