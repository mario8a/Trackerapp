package com.mario8a.trackerapp.presentation.maps

sealed interface TrackingIntent {
    data object StartTracking: TrackingIntent
    data object PauseTrack: TrackingIntent
    data object ResumeTracking: TrackingIntent
}