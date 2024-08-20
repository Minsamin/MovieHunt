package com.rpimx.moviehunt.features.home.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.common.utils.Constants.PAGING_SIZE
import com.rpimx.moviehunt.features.home.data.paging.AiringTodayTvSeriesSource
import com.rpimx.moviehunt.features.home.data.paging.OnTheAirSeriesSource
import com.rpimx.moviehunt.features.home.data.paging.PopularSeriesSource
import com.rpimx.moviehunt.features.home.data.paging.TopRatedSeriesSource
import com.rpimx.moviehunt.features.home.data.paging.TrendingSeriesSource
import com.rpimx.moviehunt.features.home.domain.models.TvSeries
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TvSeriesRepository @Inject constructor(private val api: Api) {
    fun getTrendingThisWeekTvSeries(): Flow<PagingData<TvSeries>> {
        return Pager(config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE), pagingSourceFactory = {
            TrendingSeriesSource(api)
        }).flow
    }

    fun getOnTheAirTvSeries(): Flow<PagingData<TvSeries>> {
        return Pager(config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE), pagingSourceFactory = {
            OnTheAirSeriesSource(api)
        }).flow
    }

    fun getTopRatedTvSeries(): Flow<PagingData<TvSeries>> {
        return Pager(config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE), pagingSourceFactory = {
            TopRatedSeriesSource(api)
        }).flow
    }

    fun getAiringTodayTvSeries(): Flow<PagingData<TvSeries>> {
        return Pager(config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE), pagingSourceFactory = {
            AiringTodayTvSeriesSource(api)
        }).flow
    }

    fun getPopularTvSeries(): Flow<PagingData<TvSeries>> {
        return Pager(config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE), pagingSourceFactory = {
            PopularSeriesSource(api)
        }).flow
    }
}