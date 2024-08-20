package com.rpimx.moviehunt.features.search.domain.repository

import androidx.paging.PagingData
import com.rpimx.moviehunt.features.search.domain.model.search.Search
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchMovieOrTv(queryParam: String): Flow<PagingData<Search>>
}