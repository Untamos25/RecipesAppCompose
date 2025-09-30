package com.pavlushinsa.recipescompapp.di

import com.pavlushinsa.recipescompapp.data.repository.AppRepositoryImpl
import com.pavlushinsa.recipescompapp.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppRepository(appRepositoryImpl: AppRepositoryImpl): AppRepository
}
