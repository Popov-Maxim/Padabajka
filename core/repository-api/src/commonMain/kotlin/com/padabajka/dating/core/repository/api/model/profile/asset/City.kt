package com.padabajka.dating.core.repository.api.model.profile.asset

data class City(
    val id: String,
    val name: String,
    val lat: Double,
    val lon: Double,
    val radiusKm: Double
)
