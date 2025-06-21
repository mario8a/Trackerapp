package com.mario8a.trackerapp.presentation.maps

import com.mario8a.trackerapp.domain.location.Location
import com.mario8a.trackerapp.domain.location.LocationWithTimestamp

data class TrackLocationState(
    val isTracking: Boolean = false,
    val isPaused: Boolean = false,
    val location: Location? = null,
    val selectedLocation: LocationWithTimestamp? = null,
    val trackingDataSegments:List<List<LocationWithTimestamp>> = emptyList(),
)