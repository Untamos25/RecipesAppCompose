package com.example.composeapp.di

import com.example.composeapp.presentation.common.AppWideEventDelegate
import com.example.composeapp.presentation.common.AppWideEventHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAppWideEventDelegate(handler: AppWideEventHandler): AppWideEventDelegate
}
