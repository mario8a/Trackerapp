package com.mario8a.trackerapp.presentation.camera

import android.graphics.Camera

sealed interface CameraIntent {
    data class takenPicture( val data: ByteArray): CameraIntent {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as takenPicture

            if (!data.contentEquals(other.data)) return false

            return true
        }

        override fun hashCode(): Int {
            return data.contentHashCode()
        }

    }

    data class SubmitCameraPermissionInfo(
        val acceptedCameraPermission: Boolean,
        val shouldShowCameraRationale: Boolean
    ): CameraIntent

    data object SavePhoto: CameraIntent
    data object CancelPreview: CameraIntent
}