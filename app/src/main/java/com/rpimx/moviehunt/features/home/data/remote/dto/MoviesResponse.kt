package com.rpimx.moviehunt.features.home.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.rpimx.moviehunt.features.home.domain.models.Movie

data class MoviesResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val searches: List<Movie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)