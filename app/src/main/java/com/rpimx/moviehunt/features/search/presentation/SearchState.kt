package com.rpimx.moviehunt.features.search.presentation

import androidx.paging.PagingData
import com.rpimx.moviehunt.features.search.domain.model.search.Search
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchState(
    val searchTerm: String = "",
    val searchResult: Flow<PagingData<Search>> = emptyFlow(),
)