package com.padabajka.dating.settings.data.source

import com.padabajka.dating.settings.data.network.DeleteMetadataDto
import com.padabajka.dating.settings.data.network.UpdateMetadataDto

internal interface MetadataRemoteDataSource {
    suspend fun updateAuthMetadata(metadataDto: UpdateMetadataDto)
    suspend fun deleteAuthMetadata(metadataDto: DeleteMetadataDto)
}
