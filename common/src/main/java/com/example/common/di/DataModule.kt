package com.example.common.di

import android.content.Context
import androidx.room.Room
import com.example.common.FEED_ITEM_DATABASE_NAME
import com.example.common.data.db.MovieDatabase
import com.example.common.data.db.dao.MovieDao
import com.example.common.data.network.BASE_URL
import com.example.common.data.network.NetworkApiService
import com.example.common.data.repositories.MoviesRepositoryImpl
import com.example.common.data.repositories.MoviesRepositoryImpl_Factory
import com.example.common.domain.repositories.MoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideNetworkApi(
        okHttpClient: OkHttpClient
    ): NetworkApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(NetworkApiService::class.java)

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): MovieDatabase {
        return Room
            .databaseBuilder(context, MovieDatabase::class.java, FEED_ITEM_DATABASE_NAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(database: MovieDatabase): MovieDao =
        database.movieDao()

    @Provides
    @Singleton
    fun provideMovieRepository(
        networkApi: NetworkApiService,
        movieDao: MovieDao
    ): MoviesRepository = MoviesRepositoryImpl(networkApi, movieDao)
}