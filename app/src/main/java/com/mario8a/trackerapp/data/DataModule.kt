package com.mario8a.trackerapp.data

import android.content.Context
import androidx.compose.ui.platform.LocalView
import com.mario8a.trackerapp.domain.location.LocationObserver
import com.mario8a.trackerapp.domain.location.LocationTracke
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun providesLocationObserver(
        @ApplicationContext
        context: Context
    ): LocationObserver {
        return AndroidLocationObserver(context)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        locationObserver: LocationObserver,
        applicationScore: CoroutineScope
    ): LocationTracke {
        return LocationTracke(
            locationObserver,
            applicationScore
        )
    }
}