package com.pavlushinsa.recipescompapp.di

import com.pavlushinsa.recipescompapp.presentation.common.AppWideEventDelegate
import com.pavlushinsa.recipescompapp.presentation.common.AppWideEventHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAppWideEventDelegate(handler: AppWideEventHandler): AppWideEventDelegate
}
