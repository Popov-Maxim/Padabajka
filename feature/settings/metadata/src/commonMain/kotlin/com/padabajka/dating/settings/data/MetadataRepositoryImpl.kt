package com.padabajka.dating.settings.data

import com.padabajka.dating.core.repository.api.metadata.AuthMetadata
import com.padabajka.dating.core.repository.api.metadata.DeviceUid
import com.padabajka.dating.core.repository.api.metadata.MetadataRepository
import com.padabajka.dating.settings.data.network.DeleteMetadataDto
import com.padabajka.dating.settings.data.network.toDto
import com.padabajka.dating.settings.data.source.MetadataRemoteDataSource
import dev.gitlive.firebase.installations.FirebaseInstallations

internal class MetadataRepositoryImpl(
    private val firebaseInstallations: FirebaseInstallations,
    private val remoteDataSource: MetadataRemoteDataSource
) : MetadataRepository {
    private var lastSentUpdate: AuthMetadata? = null

    override suspend fun getDeviceUid(): DeviceUid {
        return firebaseInstallations.getId().run(::DeviceUid)
    }

    override suspend fun updateAuthMetadata(authMetadata: AuthMetadata) {
        if (authMetadata == lastSentUpdate) return

        remoteDataSource.updateAuthMetadata(authMetadata.toDto())
        lastSentUpdate = authMetadata
    }

    override suspend fun deleteAuthMetadata(deviceUid: DeviceUid) {
        val dto = DeleteMetadataDto(deviceUid.raw)
        remoteDataSource.deleteAuthMetadata(dto)
        lastSentUpdate = null
    }
}
