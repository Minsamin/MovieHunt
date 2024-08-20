package com.rpimx.moviehunt.common.domain.model

import com.rpimx.moviehunt.features.bookmark.data.local.Bookmark
import kotlinx.serialization.Serializable

@Serializable
data class Film(
    val id: Int,
    val type: String,
    val image: String,
    val category: String,
    val name: String,
    val rating: Float,
    val releaseDate: String,
    val overview: String
)

fun Bookmark.toFilm() = Film(
    id = id,
    type = type,
    image = image,
    category = "",
    name = title,
    rating = rating,
    releaseDate = releaseDate,
    overview = overview
)
