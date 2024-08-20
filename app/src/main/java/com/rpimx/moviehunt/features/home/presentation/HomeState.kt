package com.rpimx.moviehunt.features.home.presentation

import androidx.paging.PagingData
import com.rpimx.moviehunt.features.home.domain.models.Movie
import com.rpimx.moviehunt.features.home.domain.models.TvSeries
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class HomeState(
    // Movies
    val trendingMovies: Flow<PagingData<Movie>> = emptyFlow(),
    val upcomingMovies: Flow<PagingData<Movie>> = emptyFlow(),
    val topRatedMovies: Flow<PagingData<Movie>> = emptyFlow(),
    val nowPlayingMovies: Flow<PagingData<Movie>> = emptyFlow(),
    val popularMovies: Flow<PagingData<Movie>> = emptyFlow(),

    // TvSeries
    val trendingTvSeries: Flow<PagingData<TvSeries>> = emptyFlow(),
    val onAirTvSeries: Flow<PagingData<TvSeries>> = emptyFlow(),
    val topRatedTvSeries: Flow<PagingData<TvSeries>> = emptyFlow(),
    val airingTodayTvSeries: Flow<PagingData<TvSeries>> = emptyFlow(),
    val popularTvSeries: Flow<PagingData<TvSeries>> = emptyFlow(),

    val selectedFilmOption: String = "Movies",
)