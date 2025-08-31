package com.example.composeapp.di

import com.example.composeapp.data.remote.source.RemoteDataSource
import com.example.composeapp.data.remote.source.RemoteDataSourceStub
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSourceStub
    }
}
