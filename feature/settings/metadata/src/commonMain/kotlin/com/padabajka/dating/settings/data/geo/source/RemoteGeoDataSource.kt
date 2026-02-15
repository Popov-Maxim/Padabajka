package com.padabajka.dating.settings.data.geo.source

import com.padabajka.dating.settings.data.geo.module.LocationRequest
import com.padabajka.dating.settings.data.geo.network.GeoApi

class RemoteGeoDataSource(
    private val geoApi: GeoApi
) {
    suspend fun sendLocation(locationRequest: LocationRequest) {
        geoApi.send(locationRequest)
    }
}
