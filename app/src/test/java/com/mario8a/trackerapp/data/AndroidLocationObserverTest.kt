package com.mario8a.trackerapp.data

import android.location.Location
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class AndroidLocationObserverTest {
    @Test
    fun `test location mapping from android Location to domain Location`() = runBlocking {
        val androidLocation = mockk<Location> {
            every { latitude } returns 10.0
            every { longitude } returns 20.0
        }
        val domainLocation = androidLocation.toLocation()
        assertEquals(10.0, domainLocation.lat, 0.0)
        assertEquals(20.0, domainLocation.long, 0.0)
    }
}
