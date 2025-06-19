package com.mario8a.trackerapp.domain.location

data class LocationData(
    val distanceMeters: Int = 0,
    // List of location segments, where each segment represents a continuous tracking period
    val locations: List<List<LocationWithTimestamp>> = emptyList(),
)