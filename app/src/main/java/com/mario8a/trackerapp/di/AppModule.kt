package com.mario8a.trackerapp.di

import android.content.Context
import com.mario8a.TrackitApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppScope(
        @ApplicationContext context: Context,
    ): CoroutineScope {
        return (context as TrackitApplication).applicationScope
    }
}