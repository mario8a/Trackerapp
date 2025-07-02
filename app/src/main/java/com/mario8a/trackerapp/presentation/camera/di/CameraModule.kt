package com.mario8a.trackerapp.presentation.camera.di

import android.content.Context
import com.mario8a.trackerapp.domain.camera.PhotoHandler
import com.mario8a.trackerapp.presentation.camera.PhotoHandlerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.net.ContentHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CameraModule {

    @Provides
    @Singleton
    fun providePhotoHandler(
        @ApplicationContext context: Context,
    ): PhotoHandler {
        return PhotoHandlerImpl(context)
    }
}