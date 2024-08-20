package com.rpimx.moviehunt.features.cast.di

import com.rpimx.moviehunt.features.cast.data.repository.CastRepositoryImpl
import com.rpimx.moviehunt.features.cast.domain.repository.CastRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CastModule {
    @Binds
    abstract fun bindCastRepository(
        castRepositoryImpl: CastRepositoryImpl
    ): CastRepository
}