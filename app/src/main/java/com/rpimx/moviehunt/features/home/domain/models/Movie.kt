package com.rpimx.moviehunt.features.home.domain.models

import com.google.gson.annotations.SerializedName
import com.rpimx.moviehunt.common.domain.model.Film
import com.rpimx.moviehunt.common.utils.Constants.TYPE_MOVIE
import com.rpimx.moviehunt.common.utils.getImageUrl

data class Movie(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("id") val id: Int,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
)

fun Movie.toFilm(
    category: String,
) = Film(
    id = id,
    type = TYPE_MOVIE,
    image = posterPath.getImageUrl(),
    category = category,
    name = title,
    rating = voteAverage.toFloat(),
    releaseDate = releaseDate,
    overview = overview
)