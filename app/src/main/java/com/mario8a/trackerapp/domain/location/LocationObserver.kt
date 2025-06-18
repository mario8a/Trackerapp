package com.mario8a.trackerapp.domain.location

import kotlinx.coroutines.flow.Flow

interface LocationObserver {
    fun obvserverLocation(interval: Long): Flow<Location>
}