package com.padabajka.dating.core.repository.api

import com.padabajka.dating.core.repository.api.model.location.Location

interface GeoRepository {
    suspend fun location(): Location

    suspend fun sendLocation(location: Location)

    suspend fun sendLocation()
}
