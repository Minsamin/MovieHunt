package com.rpimx.moviehunt.features.home.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.features.home.domain.models.Movie
import retrofit2.HttpException
import java.io.IOException

class NowPlayingMoviesSource(private val api: Api) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val nextPage = params.key ?: 1
            val nowPlayingMovies = api.getNowPlayingMovies(nextPage)
            LoadResult.Page(
                data = nowPlayingMovies.searches, prevKey = if (nextPage == 1) null else nextPage - 1, nextKey = if (nowPlayingMovies.searches.isEmpty()) null else nowPlayingMovies.page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}
