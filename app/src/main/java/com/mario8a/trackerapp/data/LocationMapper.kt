package com.mario8a.trackerapp.data

import android.location.Location

fun Location.toLocation() = com.mario8a.trackerapp.domain.location.Location(
    lat = latitude,
    long = longitude
)