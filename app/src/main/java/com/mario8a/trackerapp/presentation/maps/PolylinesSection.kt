package com.mario8a.trackerapp.presentation.maps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline
import com.mario8a.trackerapp.domain.location.LocationWithTimestamp

@Composable
fun PolylinesSection(
    location: List<List<LocationWithTimestamp>>
) {

    val polylines = remember(location) {
        location.map { location ->
            location.zipWithNext { timeStamp1, timeStamp2 ->
                PolylinesUI(
                    location1 = timeStamp1.location,
                    location2 = timeStamp2.location
                )
            }
        }
    }

    polylines.forEach { polyline ->
        polyline.forEach { polylineUi ->
            Polyline(
                points = listOf(
                    LatLng(polylineUi.location1.lat, polylineUi.location1.long),
                    LatLng(polylineUi.location2.lat, polylineUi.location2.long)
                ),
                color = Color.Blue,
                jointType = JointType.ROUND
            )
        }
    }
}