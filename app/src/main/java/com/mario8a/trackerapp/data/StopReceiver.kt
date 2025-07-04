package com.mario8a.trackerapp.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mario8a.trackerapp.TrackitService
import com.mario8a.trackerapp.domain.camera.PhotoHandler
import com.mario8a.trackerapp.domain.location.LocationTracke
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StopReceiver: BroadcastReceiver() {

    @Inject
    lateinit var photoHandler: PhotoHandler
    @Inject
    lateinit var locationTracker: LocationTracke
    @Inject
    lateinit var applicationScope: CoroutineScope

    override fun onReceive(context: Context?, intent: Intent?) {
        val receiver = intent?.action?:return

        if (receiver != ACTION_STOP) {
            return
        }
        else {
            applicationScope.launch {
                applicationScope.launch {
                    locationTracker.setIsTracking(false)
                    locationTracker.stopObservingLocation()
                    photoHandler.clearPhotos()
                }.join()
            }

            val serviceIntent = Intent(context, TrackitService::class.java).apply {
                action = TrackitService.ACTION_STOP
            }
            if(TrackitService.isServiceActive.value) {
                context?.startService(serviceIntent)
            }
        }
    }

    companion object{
        const val ACTION_STOP = "com.mario8a.trackerapp.action.STOP_TRACKING"
    }
}