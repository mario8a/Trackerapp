package com.mario8a.trackerapp.presentation.camera

import java.io.File

data class CameraUiState(
    val isInPreviewMode: Boolean = false,
    val lastSavedPhoto: File? = null,
    val showCameraRationale: Boolean = false,
    val permissionGranted: Boolean = false
)