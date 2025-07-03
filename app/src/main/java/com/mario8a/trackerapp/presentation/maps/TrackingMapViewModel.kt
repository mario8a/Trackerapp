package com.mario8a.trackerapp.presentation.maps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mario8a.trackerapp.domain.location.LocationTracke
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingMapViewModel @Inject constructor (
    private val locationTracker: LocationTracke,
) : ViewModel() {

    private val hasLocationPermission = MutableStateFlow(true)
    private val shouldTrack = MutableStateFlow(false)

    private val _event= Channel<TrackingEvents>()
    val events = _event.receiveAsFlow()

    private val _state = MutableStateFlow(TrackLocationState())
    val state = _state


    private val isAllowedToTrack = combine(
        hasLocationPermission,
        shouldTrack
    ){ hasPermission, shouldTrack->
        hasPermission && shouldTrack
    }.onEach { isAllowedToTrack ->
        if(isAllowedToTrack){
            locationTracker.startObservingLocation()
        }else{
            locationTracker.stopObservingLocation()
        }
    }

    init {

        isAllowedToTrack
            .onEach {
                updateState { state ->
                    state.copy(
                        isPaused = !it
                    )
                }
            }
            .launchIn(viewModelScope)

        locationTracker.isTracking.onEach {
            updateState { state ->
                state.copy(
                    isTracking = it
                )
            }
        }.launchIn(viewModelScope)


        locationTracker
            .currentLocation
            .onEach { location->
                updateState { state ->
                    state.copy(
                        location = location
                    )
                }
            }
            .launchIn(viewModelScope)

        locationTracker
            .locationData
            .onEach {
                updateState { state ->
                    state.copy(
                        trackingDataSegments = it.locations
                    )
                }
            }.launchIn(viewModelScope)
    }

//    val state : StateFlow<TrackLocationState> = _state
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = TrackLocationState()
//        )

    fun onAction(intent: TrackingIntent){
        viewModelScope.launch {
            when(intent){
                TrackingIntent.StartTracking -> {
                    shouldTrack.value = true
                    locationTracker.setIsTracking(true)
                }
                TrackingIntent.PauseTrack -> {
                    shouldTrack.value = false

                }
                TrackingIntent.ResumeTracking ->
                {
                    shouldTrack.value = true
                    locationTracker.setIsTracking(true)
                }

                is TrackingIntent.SubmitLocationPermissionInfo -> {
                    hasLocationPermission.value = intent.acceptedLocationPermission
                    updateState {
                        it.copy(
                            showLocationRationale = intent.showLocationRationale
                        )
                    }
                }
                is TrackingIntent.SubmitNotificationPermissionInfo -> {
                    updateState {
                        it.copy(
                            showNotificationRationale = intent.showNotificationRationale
                        )
                    }
                }

                TrackingIntent.GoToCamera -> {
                    shouldTrack.value = false
                    _event.send(TrackingEvents.NavigateToCamera)
                }

                TrackingIntent.DismissDialogLocation ->
                    updateState {
                        it.copy(
                            selectedLocation = null
                        )
                    }
                is TrackingIntent.SelectLocation -> {
                    updateState {
                        it.copy(
                            selectedLocation = intent.location
                        )
                    }
                }
            }
        }

    }

    private fun updateState(update: (TrackLocationState) -> TrackLocationState) {
        _state.update { update(it) }
    }
}