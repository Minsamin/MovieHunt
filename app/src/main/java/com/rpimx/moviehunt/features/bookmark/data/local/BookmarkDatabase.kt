package com.rpimx.moviehunt.features.bookmark.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Bookmark::class], version = 2, exportSchema = true)
abstract class BookmarkDatabase : RoomDatabase() {
    abstract val dao: BookmarkDao
}