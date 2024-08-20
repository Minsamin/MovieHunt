package com.rpimx.moviehunt.features.home.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.features.home.domain.models.TvSeries
import retrofit2.HttpException
import java.io.IOException

class PopularSeriesSource(private val api: Api) : PagingSource<Int, TvSeries>() {
    override fun getRefreshKey(state: PagingState<Int, TvSeries>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TvSeries> {
        return try {
            val nextPage = params.key ?: 1
            val popularSeries = api.getPopularTvSeries(nextPage)
            LoadResult.Page(
                data = popularSeries.results, prevKey = if (nextPage == 1) null else nextPage - 1, nextKey = if (popularSeries.results.isEmpty()) null else popularSeries.page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}
