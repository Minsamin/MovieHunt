package com.rpimx.moviehunt.features.home.presentation

import com.rpimx.moviehunt.common.domain.model.Film

sealed interface HomeEvents {
    data object NavigateBack : HomeEvents
    data object OnPullToRefresh : HomeEvents
    data class NavigateToDetails(val film: Film, ) : HomeEvents
    data class OnFilmOptionSelected(val item: String) : HomeEvents
}