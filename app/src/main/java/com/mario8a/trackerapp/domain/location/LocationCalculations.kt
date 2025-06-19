package com.mario8a.trackerapp.domain.location

import kotlin.math.roundToInt

object LocationCalculations {

    fun getTotalDistanceMeters(locations: List<List<LocationWithTimestamp>>):Int {
        return locations
            .sumOf { timestampLocation ->
                timestampLocation.zipWithNext{location1, location2 ->
                    location1.location.distanceTo(location2.location)
                }.sum().roundToInt()
            }
    }
}