package com.mario8a.trackerapp.domain.location

import java.io.File
import java.sql.Timestamp

data class LocationWithTimestamp(
    val location: Location,
    val timestamp: Timestamp,
    val listPhotos: List<File> = emptyList()
)