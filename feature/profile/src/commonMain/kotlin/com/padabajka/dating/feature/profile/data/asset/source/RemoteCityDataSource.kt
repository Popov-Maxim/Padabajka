package com.padabajka.dating.feature.profile.data.asset.source

import com.padabajka.dating.feature.profile.data.asset.model.AssetsDto
import com.padabajka.dating.feature.profile.data.asset.model.CityDto

interface RemoteCityDataSource {
    suspend fun loadCities(clientVersion: Int): AssetsDto<CityDto>?
}
