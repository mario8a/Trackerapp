package com.mario8a.trackerapp.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.mario8a.trackerapp.domain.location.Location
import com.mario8a.trackerapp.domain.location.LocationObserver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class AndroidLocationObserver @Inject constructor(
    private val context: Context
) : LocationObserver {

    private val client = LocationServices.getFusedLocationProviderClient(context)

    override fun obvserverLocation(interval: Long): Flow<Location> {
        return callbackFlow {
            // verificar que la red y la localizacion esten activar
            val locationManager = context.getSystemService<LocationManager>()!!
            var isGpsEnabled = false
            var isNetworkEnable = false
            // Verificar que los permisos esten en orden para usar la localizacion
            while (!!isGpsEnabled && !isNetworkEnable) {
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnable =
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                if (!isGpsEnabled && !isNetworkEnable) {
                    delay(3000)
                }
            }
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                close()
            } else {
                // Si tiene los permisos
                client.lastLocation.addOnSuccessListener {
                    it?.let { location ->
                        trySend(location.toLocation())
                    }
                }

                val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval)
                    .build()

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)
                        result.locations.lastOrNull()?.let { location ->
                            trySend(location.toLocation())
                        }
                    }
                }

                client.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())

                awaitClose {
                    client.removeLocationUpdates(locationCallback)
                }
            }
            // Hacer el mapeo de los modelos de localizacion
        }
    }
}