package com.rpimx.moviehunt.common.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.rpimx.moviehunt.BuildConfig
import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.common.data.repository.PreferenceRepositoryImpl
import com.rpimx.moviehunt.common.domain.repository.PreferenceRepository
import com.rpimx.moviehunt.common.utils.Constants
import com.rpimx.moviehunt.common.utils.Constants.BASE_URL
import com.rpimx.moviehunt.common.utils.Constants.DB_NAME
import com.rpimx.moviehunt.features.bookmark.data.local.BookmarkDatabase
import com.rpimx.moviehunt.features.details.data.repository.DetailsRepository
import com.rpimx.moviehunt.features.home.data.repository.MoviesRepository
import com.rpimx.moviehunt.features.home.data.repository.TvSeriesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).addInterceptor(Interceptor { chain ->
                chain.run {
                    proceed(
                        request().newBuilder().addHeader("Authorization", BuildConfig.API_KEY).build()
                    )
                }
            }).callTimeout(60, TimeUnit.SECONDS).connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)

        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun providesApi(okHttpClient: OkHttpClient): Api {
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build().create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideDatastorePreferences(@ApplicationContext context: Context): DataStore<Preferences> = PreferenceDataStoreFactory.create(produceFile = {
        context.preferencesDataStoreFile(Constants.MOVIE_HUNT_PREFERENCES)
    })

    @Provides
    @Singleton
    fun providePreferenceRepository(dataStore: DataStore<Preferences>): PreferenceRepository = PreferenceRepositoryImpl(dataStore)


    @Provides
    @Singleton
    fun provideMoviesRepository(api: Api) = MoviesRepository(api)

    @Provides
    @Singleton
    fun provideTvSeriesRepository(api: Api) = TvSeriesRepository(api)


    @Provides
    @Singleton
    fun provideBookmarksDatabase(application: Application): BookmarkDatabase {
        return Room.databaseBuilder(
            application.applicationContext, BookmarkDatabase::class.java, DB_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDetailsRepository(api: Api) = DetailsRepository(api)
}