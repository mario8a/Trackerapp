package com.mario8a.trackerapp.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object Timer {
    fun timeAndEmits(): Flow<Duration> {
        // El proposito de este metodo es emitir un flujo cada cierto tiempo
        return flow {
            var lastEmitTime = System.currentTimeMillis()
            while (true) {
                delay(200L)
                val currentTime = System.currentTimeMillis()
                // Esto va verificar que el tiempo transcurrido sea mayo a 200ms
                val elapsedTime = currentTime - lastEmitTime
                emit(elapsedTime.milliseconds)
                lastEmitTime = currentTime
            }
        }
    }

    fun randomFlow(): Flow<Int> {
        return flow {
            while (true) {
                delay(1000L)
                emit((0..100).random())
            }
        }
    }
}