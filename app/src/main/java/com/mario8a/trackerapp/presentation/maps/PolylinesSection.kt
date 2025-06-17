package com.mario8a.trackerapp.presentation.maps

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline

@Composable
fun  PolylinesSection(
    modifier: Modifier = Modifier,
    latLngArray: List<LatLng>
) {

    val newArray = latLngArray.zipWithNext()

    newArray.forEach { pair ->
        Polyline(
            points = listOf(
                pair.first,
                pair.second
            ),
            color = Color.Blue,
            jointType = JointType.ROUND
        )
    }
}