package com.mario8a.trackerapp.presentation.utils

import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat

// Las buenas practicas indican que los permisos se deben pedir cuando se necesitan, no la inicio de la app
fun ComponentActivity.shouldShowLocationRationalePermission(): Boolean {
    return shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)
}

// Notifications
fun ComponentActivity.shouldShowNotificationRationalePermission(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shouldShowRequestPermissionRationale(
        android.Manifest.permission.POST_NOTIFICATIONS
    )
}

// Camera
fun ComponentActivity.shouldShowCameraRationalePermission(): Boolean {
    return shouldShowRequestPermissionRationale(
        android.Manifest.permission.CAMERA
    )
}

private fun Context.hasPermission(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission
    ) == android.content.pm.PackageManager.PERMISSION_GRANTED
}

fun Context.hasLocationPermission(): Boolean {
    return hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
}

fun Context.hasNotificationPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    } else true
}

fun Context.hasCameraPermission(): Boolean {
    return hasPermission(android.Manifest.permission.CAMERA)
}