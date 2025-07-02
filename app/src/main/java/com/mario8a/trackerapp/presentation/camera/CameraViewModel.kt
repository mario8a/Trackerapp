package com.mario8a.trackerapp.presentation.camera

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mario8a.trackerapp.domain.camera.PhotoHandler
import com.mario8a.trackerapp.domain.location.LocationTracke
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val photoHandler: PhotoHandler,
    private val locationTracker: LocationTracke
): ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState = _uiState.asStateFlow()

    val previewPhoto: StateFlow<ByteArray?> = photoHandler.getCurrentPreviewPhoto()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun onAction(cameraIntent: CameraIntent) {
        viewModelScope.launch {
            when(cameraIntent) {
                CameraIntent.CancelPreview -> cancelPreview()
                CameraIntent.SavePhoto -> savePhoto()
                is CameraIntent.SubmitCameraPermissionInfo -> {
                    _uiState.value = _uiState.value.copy(
                        showCameraRationale = cameraIntent.shouldShowCameraRationale,
                        permissionGranted = cameraIntent.acceptedCameraPermission
                    )
                }

                is CameraIntent.takenPicture -> processPhoto(cameraIntent.data)
            }
        }
    }

    private suspend fun savePhoto() {
        val savedFile = photoHandler.savePicturePreview()

        if(savedFile != null) {
            associatedWithLatestlocation(savedFile)
        }
        _uiState.value = _uiState.value.copy(
            isInPreviewMode = false,
            lastSavedPhoto = savedFile
        )
    }

    private suspend fun associatedWithLatestlocation(photoFile: File) {
        val locationData = locationTracker.locationData.value

        val lastNonEmptySegmentIndex = locationData.locations.indexOfLast { it.isNotEmpty() }

        if (lastNonEmptySegmentIndex == -1) return

        val lastNonEmptySegment = locationData.locations[lastNonEmptySegmentIndex]

        val latestLocation = lastNonEmptySegment.last()

        val updatedLocation = latestLocation.copy(
            listPhotos = latestLocation.listPhotos + photoFile
        )

        val updatedSegment = lastNonEmptySegment.dropLast(1) + updatedLocation

        val updateSegment = locationData.locations.toMutableList().apply {
            set(lastNonEmptySegmentIndex, updatedSegment)
        }

        locationTracker.updateLocationData(
            locationData.copy(locations = updateSegment)
        )
    }

    private suspend fun processPhoto(data: ByteArray) {
        photoHandler.onPhotoPreview(photoBytes = data)
        _uiState.value = _uiState.value.copy(isInPreviewMode = true)
    }

    private suspend fun cancelPreview() {
        photoHandler.onCancelPreview()
        _uiState.value = _uiState.value.copy(isInPreviewMode = false)
    }
}