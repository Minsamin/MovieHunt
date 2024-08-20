package com.rpimx.moviehunt.features.search.di

import com.rpimx.moviehunt.features.search.data.repository.SearchRepositoryImpl
import com.rpimx.moviehunt.features.search.domain.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchModule {
    @Binds
    abstract fun bindSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}