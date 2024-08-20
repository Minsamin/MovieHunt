package com.rpimx.moviehunt.features.details.presentation

import com.rpimx.moviehunt.features.cast.domain.model.Credits
import com.rpimx.moviehunt.features.home.data.remote.dto.MovieDetails
import com.rpimx.moviehunt.features.home.data.remote.dto.TvSeriesDetails

data class DetailsState(
    val credits: Credits? = null,
    val isLoading: Boolean = false,
    val isLoadingCasts: Boolean = false,
    val error: String? = null,
    val errorCasts: String? = null,
    val tvSeriesDetails: TvSeriesDetails? = null,
    val movieDetails: MovieDetails? = null
)