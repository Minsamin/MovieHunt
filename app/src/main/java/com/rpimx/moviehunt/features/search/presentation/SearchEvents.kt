package com.rpimx.moviehunt.features.search.presentation

import com.rpimx.moviehunt.features.search.domain.model.search.Search

sealed interface SearchEvents {
    data class SearchTermChanged(val value: String) : SearchEvents
    data class SearchFilm(val searchTerm: String) : SearchEvents
    data class OpenFilmDetails(val search: Search?) : SearchEvents
    data object ClearSearchTerm : SearchEvents
}