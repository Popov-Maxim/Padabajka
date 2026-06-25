package com.padabajka.dating.settings.data.network

internal interface AuthMetadataApi {

    /**
     * PATCH /metadata
     */
    suspend fun patch(metadataDto: UpdateMetadataDto)

    /**
     * DELETE /metadata
     */
    suspend fun delete(metadataDto: DeleteMetadataDto)

    companion object {
        const val PATH = "metadata"
    }
}
