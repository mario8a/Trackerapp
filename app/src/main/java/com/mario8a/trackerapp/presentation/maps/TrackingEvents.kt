package com.mario8a.trackerapp.presentation.maps

sealed interface TrackingEvents{
    data object NavigateToCamera: TrackingEvents
}