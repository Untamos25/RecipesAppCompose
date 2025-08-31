package com.example.composeapp.di

import com.example.composeapp.data.repository.AppRepositoryImpl
import com.example.composeapp.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppRepository(appRepositoryImpl: AppRepositoryImpl): AppRepository
}
