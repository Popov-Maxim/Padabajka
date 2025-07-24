package com.padabajka.dating.feature.profile.data.asset.source

import com.padabajka.dating.feature.profile.data.asset.model.CityDto

interface RemoteCityDataSource {
    suspend fun loadCities(): List<CityDto>
}
