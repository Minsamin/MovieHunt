package com.rpimx.moviehunt.features.home.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.common.utils.Constants.PAGING_SIZE
import com.rpimx.moviehunt.features.home.data.paging.NowPlayingMoviesSource
import com.rpimx.moviehunt.features.home.data.paging.PopularMoviesSource
import com.rpimx.moviehunt.features.home.data.paging.TopRatedMoviesSource
import com.rpimx.moviehunt.features.home.data.paging.TrendingMoviesSource
import com.rpimx.moviehunt.features.home.data.paging.UpcomingMoviesSource
import com.rpimx.moviehunt.features.home.domain.models.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRepository @Inject constructor(private val api: Api) {

    fun getTrendingMoviesThisWeek(): Flow<PagingData<Movie>> {
        return Pager(config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE), pagingSourceFactory = {
            TrendingMoviesSource(api)
        }).flow
    }

    fun getUpcomingMovies(): Flow<PagingData<Movie>> {
        return Pager(config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE), pagingSourceFactory = {
            UpcomingMoviesSource(api)
        }).flow
    }

    fun getTopRatedMovies(): Flow<PagingData<Movie>> {
        return Pager(config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE), pagingSourceFactory = {
            TopRatedMoviesSource(api)
        }).flow
    }

    fun getNowPlayingMovies(): Flow<PagingData<Movie>> {
        return Pager(config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE), pagingSourceFactory = {
            NowPlayingMoviesSource(api)
        }).flow
    }

    fun getPopularMovies(): Flow<PagingData<Movie>> {
        return Pager(config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE), pagingSourceFactory = {
            PopularMoviesSource(api)
        }).flow
    }
}