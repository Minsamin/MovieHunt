package com.rpimx.moviehunt.features.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rpimx.moviehunt.common.utils.Resource
import com.rpimx.moviehunt.features.bookmark.data.local.Bookmark
import com.rpimx.moviehunt.features.bookmark.domain.repository.BookmarkRepository
import com.rpimx.moviehunt.features.cast.domain.usecases.GetMovieCastUseCase
import com.rpimx.moviehunt.features.cast.domain.usecases.GetTvCastUseCase
import com.rpimx.moviehunt.features.details.data.repository.DetailsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: DetailsRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val getTvCastUseCase: GetTvCastUseCase,
    private val getMovieCastUseCase: GetMovieCastUseCase,
) : ViewModel() {
    private val _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()

    private fun getMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(
                    isLoading = true
                )
            }
            when (val result = repository.getMoviesDetails(movieId)) {
                is Resource.Error -> {
                    _detailsState.update {
                        it.copy(
                            isLoading = false, error = result.message
                        )
                    }
                }

                is Resource.Success -> {
                    _detailsState.update {
                        it.copy(
                            isLoading = false, movieDetails = result.data
                        )
                    }
                }

                else -> {
                    detailsState
                }
            }
        }
    }

    private fun getTvSeriesDetails(tvId: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(
                    isLoading = true
                )
            }
            when (val result = repository.getTvSeriesDetails(tvId)) {
                is Resource.Error -> {
                    _detailsState.update {
                        it.copy(
                            isLoading = false, error = result.message
                        )
                    }
                }

                is Resource.Success -> {
                    _detailsState.update {
                        it.copy(
                            isLoading = false, tvSeriesDetails = result.data
                        )
                    }
                }

                else -> {
                    detailsState
                }
            }
        }
    }

    private fun getMovieCasts(movieId: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoadingCasts = true)
            }
            when (val result = getMovieCastUseCase(movieId)) {
                is Resource.Error -> {
                    _detailsState.update {
                        it.copy(
                            isLoadingCasts = false, errorCasts = result.message
                        )
                    }
                }

                is Resource.Success -> {
                    _detailsState.update {
                        it.copy(
                            isLoadingCasts = false, credits = result.data
                        )
                    }
                }

                else -> {
                    detailsState
                }
            }
        }
    }

    private fun getTvSeriesCasts(tvId: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoadingCasts = true)
            }
            when (val result = getTvCastUseCase(tvId)) {
                is Resource.Error -> {
                    _detailsState.update {
                        it.copy(
                            isLoadingCasts = false, errorCasts = result.message
                        )
                    }
                }

                is Resource.Success -> {
                    _detailsState.update {
                        it.copy(
                            isLoadingCasts = false, credits = result.data
                        )
                    }
                }

                else -> {
                    detailsState
                }
            }
        }
    }

    fun getFilmDetails(filmId: Int, filmType: String) {
        if (filmType == "movie") {
            getMovieDetails(filmId)
            getMovieCasts(filmId)
        } else {
            getTvSeriesDetails(filmId)
            getTvSeriesCasts(filmId)
        }
    }

    fun isBookmarked(mediaId: Int): Flow<Boolean> {
        return bookmarkRepository.isBookmarked(mediaId)
    }

    fun addToBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            bookmarkRepository.addToBookmark(bookmark)
        }
    }

    fun deleteFromBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            bookmarkRepository.deleteFromBookmark(bookmark)
        }
    }
}