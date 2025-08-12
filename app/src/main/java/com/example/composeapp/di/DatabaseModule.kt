package com.example.composeapp.di

import android.content.Context
import androidx.room.Room
import com.example.composeapp.data.local.AppDatabase
import com.example.composeapp.data.local.categories.dao.CategoryDao
import com.example.composeapp.data.local.recipes.dao.RecipeDao
import com.example.composeapp.data.remote.source.RemoteDataSource
import com.example.composeapp.data.remote.source.RemoteDataSourceStub
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    @Singleton
    fun provideRecipeDao(database: AppDatabase): RecipeDao {
        return database.recipeDao()
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(): RemoteDataSource {
        return RemoteDataSourceStub
    }
}
