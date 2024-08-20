package com.rpimx.moviehunt.features.bookmark.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rpimx.moviehunt.common.utils.Constants.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Bookmark(
    @PrimaryKey val id: Int,
    val title: String,
    val type: String,
    val image: String,
    val rating: Float,
    val bookmark: Boolean,
    val overview: String,
    val releaseDate: String,
)