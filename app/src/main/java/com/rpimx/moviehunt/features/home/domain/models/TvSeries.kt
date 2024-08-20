package com.rpimx.moviehunt.features.home.domain.models

import com.google.gson.annotations.SerializedName
import com.rpimx.moviehunt.common.domain.model.Film
import com.rpimx.moviehunt.common.utils.Constants.TYPE_TV_SERIES
import com.rpimx.moviehunt.common.utils.getImageUrl

data class TvSeries(
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("first_air_date") val firstAirDate: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("origin_country") val originCountry: List<String>,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_name") val originalName: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
)

fun TvSeries.toFilm(
    category: String,
) = Film(
    id = id,
    type = TYPE_TV_SERIES,
    image = posterPath.getImageUrl(),
    category = category,
    name = name,
    rating = voteAverage.toFloat(),
    releaseDate = firstAirDate,
    overview = overview
)