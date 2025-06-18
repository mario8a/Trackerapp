package com.mario8a.trackerapp.domain.location

data class LocationData(
    val distancemeters: Int = 0,
    val locations: List<List<LocationWithTimestamp>> = emptyList()
)