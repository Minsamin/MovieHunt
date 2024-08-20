package com.rpimx.moviehunt.features.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.common.utils.Constants.PAGING_SIZE
import com.rpimx.moviehunt.features.search.data.paging.SearchPagingSource
import com.rpimx.moviehunt.features.search.domain.model.search.Search
import com.rpimx.moviehunt.features.search.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val api: Api) : SearchRepository {
    override fun searchMovieOrTv(queryParam: String): Flow<PagingData<Search>> {
        return Pager(config = PagingConfig(enablePlaceholders = false, pageSize = PAGING_SIZE), pagingSourceFactory = {
            SearchPagingSource(api, queryParam)
        }).flow
    }
}