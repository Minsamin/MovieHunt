package com.rpimx.moviehunt.features.bookmark.data.repository

import com.rpimx.moviehunt.features.bookmark.data.local.Bookmark
import com.rpimx.moviehunt.features.bookmark.data.local.BookmarkDatabase
import com.rpimx.moviehunt.features.bookmark.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(private val database: BookmarkDatabase) : BookmarkRepository {
    override suspend fun addToBookmark(bookmark: Bookmark) {
        database.dao.addToBookmark(bookmark)
    }

    override fun getAllBookmarks(): Flow<List<Bookmark>> {
        return database.dao.getAllBookmarks()
    }

    override fun isBookmarked(mediaId: Int): Flow<Boolean> {
        return database.dao.isBookmarked(mediaId)
    }

    override fun getABookmarks(mediaId: Int): Flow<Bookmark?> {
        return database.dao.getABookmark(mediaId)
    }

    override suspend fun deleteFromBookmark(bookmark: Bookmark) {
        database.dao.deleteABookmark(bookmark)
    }

    override suspend fun deleteAllBookmarks() {
        database.dao.deleteAllBookmarks()
    }
}