package com.mario8a.trackerapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import com.mario8a.trackerapp.domain.location.LocationTracke
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale
import javax.inject.Inject
import kotlin.time.Duration

@AndroidEntryPoint
class TrackitService : Service() {

    private val notificationManager by lazy {
        getSystemService<NotificationManager>()!!
    }
    private val baseNotification by lazy {
        NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(getString(R.string.active_run))
    }

    override fun onBind(p0: Intent?): IBinder? = null

    @Inject
    lateinit var locationTracker: LocationTracke

    private var serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                start()
            }

            ACTION_STOP -> {
                stop()
            }
        }
        return START_STICKY
    }

    private fun start() {
        if (!_isServiceActive.value) {
            createNotificationChannel()

            val activityIntent = Intent(applicationContext, MainActivity::class.java).apply {
                data = "trackit_app://map".toUri()
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }

            val pendingIntent = TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(activityIntent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

            val notification = baseNotification
                .setContentText("00:00:00")
                .setContentIntent(pendingIntent)
                .build()
            startForeground(1, notification)
            _isServiceActive.value = true
            locationTracker.elapsedTime.onEach { elapsedTime ->
                val notification = baseNotification
                    .setContentText(elapsedTime.formatted())
                    .build()
                startForeground(1, notification)
            }.launchIn(serviceScope)
        }
    }

    fun stop() {
        stopSelf()
        _isServiceActive.value = false
        serviceScope.cancel()
        serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            getString(R.string.active_run),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private val _isServiceActive = MutableStateFlow(false)
        val isServiceActive = _isServiceActive.asStateFlow()
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val CHANNEL_ID = "trackit_notification_channel"
    }
}

fun Duration.formatted(): String {
    val totalSeconds = inWholeSeconds
    val hours = String.format(Locale.US,"%02d", totalSeconds / (60 * 60))
    val minutes = String.format(Locale.US,"%02d", (totalSeconds % 3600) / 60)
    val seconds = String.format(Locale.US,"%02d", (totalSeconds % 60))

    return "$hours:$minutes:$seconds"
}