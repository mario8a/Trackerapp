package com.mario8a.trackerapp.presentation.maps

sealed interface TrackingIntent {
    data class SubmitLocationPermissionInfo(
        val acceptedLocationPermission: Boolean,
        val showLocationRationale: Boolean,
    ) : TrackingIntent

    data class SubmitNotificationPermissionInfo(
        val acceptedNotificationPermission: Boolean,
        val showNotificationRationale: Boolean,
    ) : TrackingIntent

    data object StartTracking : TrackingIntent
    data object PauseTrack : TrackingIntent
    data object ResumeTracking : TrackingIntent
    companion object
}