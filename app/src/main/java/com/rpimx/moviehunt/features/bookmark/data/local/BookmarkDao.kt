package com.rpimx.moviehunt.features.bookmark.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Insert
    suspend fun addToBookmark(bookmark: Bookmark)

    @Query("SELECT * FROM movie_hunt_fav_table ORDER BY id DESC")
    fun getAllBookmarks(): Flow<List<Bookmark>>

    @Query("SELECT * FROM movie_hunt_fav_table WHERE id  == :mediaId")
    fun getABookmark(mediaId: Int): Flow<Bookmark?>

    @Query("SELECT bookmark FROM movie_hunt_fav_table WHERE id = :mediaId")
    fun isBookmarked(mediaId: Int): Flow<Boolean>

    @Delete
    suspend fun deleteABookmark(bookmark: Bookmark)

    @Query("DELETE FROM movie_hunt_fav_table")
    suspend fun deleteAllBookmarks()
}