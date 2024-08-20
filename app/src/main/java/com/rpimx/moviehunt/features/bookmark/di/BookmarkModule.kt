package com.rpimx.moviehunt.features.bookmark.di


import com.rpimx.moviehunt.features.bookmark.data.repository.BookmarkRepositoryImpl
import com.rpimx.moviehunt.features.bookmark.domain.repository.BookmarkRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BookmarkModule {
    @Binds
    abstract fun bindBookmarksRepository(
        bookmarksRepositoryImpl: BookmarkRepositoryImpl
    ): BookmarkRepository
}