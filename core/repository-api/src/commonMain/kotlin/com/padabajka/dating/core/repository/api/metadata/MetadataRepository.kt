package com.padabajka.dating.core.repository.api.metadata

interface MetadataRepository {
    suspend fun getDeviceUid(): DeviceUid

    suspend fun updateAuthMetadata(authMetadata: AuthMetadata)
    suspend fun deleteAuthMetadata(deviceUid: DeviceUid)
}
