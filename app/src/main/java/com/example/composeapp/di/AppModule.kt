package com.example.composeapp.di

import com.example.composeapp.data.repository.AppRepositoryImpl
import com.example.composeapp.data.repository.source.AppRepositoryStub
import com.example.composeapp.domain.repository.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRecipesRepositoryStub(): AppRepositoryStub {
        return AppRepositoryStub
    }

    @Provides
    @Singleton
    fun provideRecipesRepository(dataSource: AppRepositoryStub): AppRepository {
        return AppRepositoryImpl(dataSource)
    }
}
