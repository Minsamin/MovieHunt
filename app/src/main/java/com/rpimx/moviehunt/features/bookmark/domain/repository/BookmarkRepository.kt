package com.rpimx.moviehunt.features.bookmark.domain.repository

import com.rpimx.moviehunt.features.bookmark.data.local.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun addToBookmark(bookmark: Bookmark)
    fun getAllBookmarks(): Flow<List<Bookmark>>
    fun isBookmarked(mediaId: Int): Flow<Boolean>
    fun getABookmarks(mediaId: Int): Flow<Bookmark?>
    suspend fun deleteFromBookmark(bookmark: Bookmark)
    suspend fun deleteAllBookmarks()
}