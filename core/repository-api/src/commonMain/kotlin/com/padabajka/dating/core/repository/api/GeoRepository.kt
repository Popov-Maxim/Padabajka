package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.location.Location
import kotlinx.coroutines.flow.Flow

interface GeoRepository {
    val location: Flow<Location>

    suspend fun sendLocation(location: Location)
}
