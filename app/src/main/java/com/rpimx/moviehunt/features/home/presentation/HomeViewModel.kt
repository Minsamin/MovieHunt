package com.rpimx.moviehunt.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import com.rpimx.moviehunt.features.home.data.repository.MoviesRepository
import com.rpimx.moviehunt.features.home.data.repository.TvSeriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val seriesRepository: TvSeriesRepository,
) : ViewModel() {
    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        refreshAllData()
    }

    fun getTrendingMovies(genreId: Int? = null) {
        _homeState.update {
            it.copy(
                trendingMovies = if (genreId != null) {
                    moviesRepository.getTrendingMoviesThisWeek().map { pagingData ->
                        pagingData.filter {
                            it.genreIds.contains(genreId)
                        }
                    }.cachedIn(viewModelScope)
                } else {
                    moviesRepository.getTrendingMoviesThisWeek().cachedIn(viewModelScope)
                }
            )
        }
    }


    fun getUpcomingMovies(genreId: Int? = null) {
        _homeState.update {
            it.copy(
                upcomingMovies = if (genreId != null) {
                    moviesRepository.getUpcomingMovies().map { pagingData ->
                        pagingData.filter {
                            it.genreIds.contains(genreId)
                        }
                    }.cachedIn(viewModelScope)
                } else {
                    moviesRepository.getUpcomingMovies().cachedIn(viewModelScope)
                }
            )
        }
    }

    fun getTopRatedMovies(genreId: Int? = null) {
        _homeState.update {
            it.copy(
                topRatedMovies = if (genreId != null) {
                    moviesRepository.getTopRatedMovies().map { pagingData ->
                        pagingData.filter {
                            it.genreIds.contains(genreId)
                        }
                    }.cachedIn(viewModelScope)
                } else {
                    moviesRepository.getTopRatedMovies().cachedIn(viewModelScope)
                }
            )
        }
    }

    fun getNowPayingMovies(genreId: Int? = null) {
        _homeState.update {
            it.copy(
                nowPlayingMovies = if (genreId != null) {
                    moviesRepository.getNowPlayingMovies().map { pagingData ->
                        pagingData.filter {
                            it.genreIds.contains(genreId)
                        }
                    }.cachedIn(viewModelScope)
                } else {
                    moviesRepository.getNowPlayingMovies().cachedIn(viewModelScope)
                }
            )
        }
    }

    fun getPopularMovies(genreId: Int? = null) {
        _homeState.update {
            it.copy(
                popularMovies = if (genreId != null) {
                    moviesRepository.getPopularMovies().map { pagingData ->
                        pagingData.filter {
                            it.genreIds.contains(genreId)
                        }
                    }.cachedIn(viewModelScope)
                } else {
                    moviesRepository.getPopularMovies().cachedIn(viewModelScope)
                }
            )
        }
    }

    fun getTrendingTvSeries(genreId: Int? = null) {
        _homeState.update {
            it.copy(
                trendingTvSeries = if (genreId != null) {
                    seriesRepository.getTrendingThisWeekTvSeries().map { pagingData ->
                        pagingData.filter {
                            it.genreIds.contains(genreId)
                        }
                    }.cachedIn(viewModelScope)
                } else {
                    seriesRepository.getTrendingThisWeekTvSeries().cachedIn(viewModelScope)
                }
            )
        }
    }

    fun getOnTheAirTvSeries(genreId: Int? = null) {
        _homeState.update {
            it.copy(
                onAirTvSeries = if (genreId != null) {
                    seriesRepository.getOnTheAirTvSeries().map { pagingData ->
                        pagingData.filter {
                            it.genreIds.contains(genreId)
                        }
                    }.cachedIn(viewModelScope)
                } else {
                    seriesRepository.getOnTheAirTvSeries().cachedIn(viewModelScope)
                }
            )
        }
    }

    fun getTopRatedTvSeries(genreId: Int? = null) {
        _homeState.update {
            it.copy(
                topRatedTvSeries = if (genreId != null) {
                    seriesRepository.getTopRatedTvSeries().map { pagingData ->
                        pagingData.filter {
                            it.genreIds.contains(genreId)
                        }
                    }.cachedIn(viewModelScope)
                } else {
                    seriesRepository.getTopRatedTvSeries().cachedIn(viewModelScope)
                }
            )
        }
    }

    fun getAiringTodayTvSeries(genreId: Int? = null) {
        _homeState.update {
            it.copy(
                airingTodayTvSeries = if (genreId != null) {
                    seriesRepository.getAiringTodayTvSeries().map { pagingData ->
                        pagingData.filter {
                            it.genreIds.contains(genreId)
                        }
                    }.cachedIn(viewModelScope)
                } else {
                    seriesRepository.getAiringTodayTvSeries().cachedIn(viewModelScope)
                }
            )
        }
    }

    private fun getPopularTvSeries(genreId: Int? = null) {
        _homeState.update {
            it.copy(
                popularTvSeries = if (genreId != null) {
                    seriesRepository.getPopularTvSeries().map { pagingData ->
                        pagingData.filter {
                            it.genreIds.contains(genreId)
                        }
                    }.cachedIn(viewModelScope)
                } else {
                    seriesRepository.getPopularTvSeries().cachedIn(viewModelScope)
                }
            )
        }
    }


    fun setSelectedOption(item: String) {
        _homeState.update {
            it.copy(
                selectedFilmOption = item
            )
        }
    }

    fun refreshAllData() {
        getTrendingMovies()
        getNowPayingMovies()
        getUpcomingMovies()
        getTopRatedMovies()
        getPopularMovies()
        getPopularTvSeries()
        getAiringTodayTvSeries()
        getTrendingTvSeries()
        getOnTheAirTvSeries()
        getTopRatedTvSeries()
        getOnTheAirTvSeries()
    }
}