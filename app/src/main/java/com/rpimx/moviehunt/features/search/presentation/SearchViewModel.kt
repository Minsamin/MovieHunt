package com.rpimx.moviehunt.features.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.rpimx.moviehunt.features.search.domain.usecase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
) : ViewModel() {

    private val _searchState = MutableStateFlow(SearchState())
    val searchState = _searchState.asStateFlow()

    private var searchJob: Job? = null

    fun searchAll(searchParam: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _searchState.update {
                it.copy(
                    searchResult = searchUseCase(searchParam).cachedIn(viewModelScope)
                )
            }
        }
    }

    fun updateSearchTerm(value: String) {
        _searchState.update {
            it.copy(searchTerm = value)
        }
    }

    fun clearSearch() {
        _searchState.update {
            it.copy(
                searchResult = emptyFlow(), searchTerm = ""
            )
        }
    }
}