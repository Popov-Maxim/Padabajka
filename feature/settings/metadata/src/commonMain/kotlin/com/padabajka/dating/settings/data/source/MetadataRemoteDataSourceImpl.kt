package com.padabajka.dating.settings.data.source

import com.padabajka.dating.settings.data.network.AuthMetadataApi
import com.padabajka.dating.settings.data.network.DeleteMetadataDto
import com.padabajka.dating.settings.data.network.UpdateMetadataDto

internal class MetadataRemoteDataSourceImpl(
    private val metadataApi: AuthMetadataApi
) : MetadataRemoteDataSource {

    override suspend fun updateAuthMetadata(metadataDto: UpdateMetadataDto) {
        metadataApi.patch(metadataDto)
    }

    override suspend fun deleteAuthMetadata(metadataDto: DeleteMetadataDto) {
        metadataApi.delete(metadataDto)
    }
}
