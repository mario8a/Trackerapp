package com.mario8a.trackerapp.presentation.maps

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import com.mario8a.trackerapp.domain.location.LocationWithTimestamp

@Composable
fun PhotoGalleryDialog(
    locationWithPhotos: LocationWithTimestamp,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        PhotoGallery(
            photos = locationWithPhotos.listPhotos,
            locationTimestamp = locationWithPhotos.timestamp
        )
    }
}