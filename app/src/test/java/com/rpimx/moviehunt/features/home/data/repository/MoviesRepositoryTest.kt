package com.rpimx.moviehunt.features.home.data.repository

import androidx.paging.PagingSource
import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.features.home.data.paging.NowPlayingMoviesSource
import com.rpimx.moviehunt.features.home.data.paging.PopularMoviesSource
import com.rpimx.moviehunt.features.home.data.paging.TopRatedMoviesSource
import com.rpimx.moviehunt.features.home.data.paging.TrendingMoviesSource
import com.rpimx.moviehunt.features.home.data.paging.UpcomingMoviesSource
import com.rpimx.moviehunt.features.home.data.remote.dto.MoviesResponse
import com.rpimx.moviehunt.features.home.domain.models.Movie
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MoviesRepositoryTest {

    @MockK
    private var api: Api = mockk<Api>()

    private lateinit var params: PagingSource.LoadParams.Refresh<Int>

    @Before
    fun setup() {
        params = PagingSource.LoadParams.Refresh(
            key = null, loadSize = 1, placeholdersEnabled = false
        )
    }

    @Test
    fun `when TrendingMoviesSource loads data then it returns expected LoadResult`() = runTest {
        // Given
        coEvery { api.getTrendingTodayMovies(any()) } returns getMovieResponse()
        val trendingMoviesSource = TrendingMoviesSource(api = api)

        val expectedMovies = listOf(getMovie())
        val expectedResult = PagingSource.LoadResult.Page(
            data = expectedMovies, prevKey = null, nextKey = 2
        )

        // When
        val actualResult = trendingMoviesSource.load(params)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when UpcomingMoviesSource loads data then it returns expected LoadResult`() = runTest {
        // Given
        coEvery { api.getUpcomingMovies(any()) } returns getMovieResponse()
        val upcomingMoviesSource = UpcomingMoviesSource(api = api)

        val expectedMovies = listOf(getMovie())
        val expectedResult = PagingSource.LoadResult.Page(
            data = expectedMovies, prevKey = null, nextKey = 2
        )

        // When
        val actualResult = upcomingMoviesSource.load(params)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when TopRatedMoviesSource loads data then it returns expected LoadResult`() = runTest {
        // Given
        coEvery { api.getTopRatedMovies(any()) } returns getMovieResponse()
        val topRatedMoviesSource = TopRatedMoviesSource(api = api)

        val expectedMovies = listOf(getMovie())
        val expectedResult = PagingSource.LoadResult.Page(
            data = expectedMovies, prevKey = null, nextKey = 2
        )

        // When
        val actualResult = topRatedMoviesSource.load(params)

        // Then
        assertEquals(expectedResult, actualResult)
    }


    @Test
    fun `when NowPlayingMoviesSource loads data then it returns expected LoadResult`() = runTest {
        // Given
        coEvery { api.getNowPlayingMovies(any()) } returns getMovieResponse()
        val nowPlayingMoviesSource = NowPlayingMoviesSource(api = api)

        val expectedMovies = listOf(getMovie())
        val expectedResult = PagingSource.LoadResult.Page(
            data = expectedMovies, prevKey = null, nextKey = 2
        )

        // When
        val actualResult = nowPlayingMoviesSource.load(params)

        // Then
        assertEquals(expectedResult, actualResult)
    }


    @Test
    fun `when PopularMoviesSource loads data then it returns expected LoadResult`() = runTest {
        // Given
        coEvery { api.getPopularMovies(any()) } returns getMovieResponse()
        val popularMoviesSource = PopularMoviesSource(api = api)

        val expectedMovies = listOf(getMovie())
        val expectedResult = PagingSource.LoadResult.Page(
            data = expectedMovies, prevKey = null, nextKey = 2
        )

        // When
        val actualResult = popularMoviesSource.load(params)

        // Then
        assertEquals(expectedResult, actualResult)
    }


    private fun getMovie() = Movie(
        adult = false, backdropPath = "/path/to/backdrop.jpg", genreIds = listOf(28, 12, 14), id = 123456, originalLanguage = "en", originalTitle = "The Example Movie", overview = "An exciting adventure of a hero on a quest to save the world. Full of thrilling moments and spectacular visuals.", popularity = 7.8, posterPath = "/path/to/poster.jpg", releaseDate = "2024-05-10", title = "The Example Movie", video = false, voteAverage = 8.5, voteCount = 4500
    )

    private fun getMovieResponse() = MoviesResponse(
        searches = listOf(getMovie()), page = 1, totalResults = 10, totalPages = 5
    )

}