package com.pavlushinsa.recipescompapp.di

import com.pavlushinsa.recipescompapp.data.remote.api.RecipesApiService
import com.pavlushinsa.recipescompapp.data.remote.source.RemoteDataSource
import com.pavlushinsa.recipescompapp.data.remote.source.RemoteDataSourceImpl
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://recipes.androidsprint.ru/api/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRecipesApiService(okHttpClient: OkHttpClient): RecipesApiService {
        val json = Json { ignoreUnknownKeys = true }
        val contentType = "application/json".toMediaType()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        return retrofit.create(RecipesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        apiService: RecipesApiService
    ): RemoteDataSource {
        return RemoteDataSourceImpl(apiService)
    }
}
