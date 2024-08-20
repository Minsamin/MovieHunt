package com.rpimx.moviehunt.features.home.data.repository

import androidx.paging.PagingSource
import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.features.home.data.paging.AiringTodayTvSeriesSource
import com.rpimx.moviehunt.features.home.data.paging.OnTheAirSeriesSource
import com.rpimx.moviehunt.features.home.data.paging.PopularSeriesSource
import com.rpimx.moviehunt.features.home.data.paging.TopRatedSeriesSource
import com.rpimx.moviehunt.features.home.data.paging.TrendingSeriesSource
import com.rpimx.moviehunt.features.home.data.remote.dto.TvSeriesResponse
import com.rpimx.moviehunt.features.home.domain.models.TvSeries
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TvSeriesRepositoryTest {

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
    fun `when TopRatedSeriesSource loads data then it returns expected LoadResult`() = runTest {
        // Given
        coEvery { api.getTopRatedTvSeries(any()) } returns getTvSeriesResponse()
        val topRatedSeriesSource = TopRatedSeriesSource(api = api)

        val expectedTvSeries = listOf(getSeries())
        val expectedResult = PagingSource.LoadResult.Page(
            data = expectedTvSeries, prevKey = null, nextKey = 2
        )

        // When
        val actualResult = topRatedSeriesSource.load(params)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when TrendingSeriesSource loads data then it returns expected LoadResult`() = runTest {
        // Given
        coEvery { api.getTrendingTvSeries(any()) } returns getTvSeriesResponse()
        val trendingSeriesSource = TrendingSeriesSource(api = api)

        val expectedTvSeries = listOf(getSeries())
        val expectedResult = PagingSource.LoadResult.Page(
            data = expectedTvSeries, prevKey = null, nextKey = 2
        )

        // When
        val actualResult = trendingSeriesSource.load(params)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when OnTheAirSeriesSource loads data then it returns expected LoadResult`() = runTest {
        // Given
        coEvery { api.getOnTheAirTvSeries(any()) } returns getTvSeriesResponse()
        val onTheAirSeriesSource = OnTheAirSeriesSource(api = api)

        val expectedTvSeries = listOf(getSeries())
        val expectedResult = PagingSource.LoadResult.Page(
            data = expectedTvSeries, prevKey = null, nextKey = 2
        )

        // When
        val actualResult = onTheAirSeriesSource.load(params)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when AiringTodayTvSeriesSource loads data then it returns expected LoadResult`() = runTest {
        // Given
        coEvery { api.getAiringTodayTvSeries(any()) } returns getTvSeriesResponse()
        val airingTodayTvSeriesSource = AiringTodayTvSeriesSource(api = api)

        val expectedTvSeries = listOf(getSeries())
        val expectedResult = PagingSource.LoadResult.Page(
            data = expectedTvSeries, prevKey = null, nextKey = 2
        )

        // When
        val actualResult = airingTodayTvSeriesSource.load(params)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when PopularSeriesSource loads data then it returns expected LoadResult`() = runTest {
        // Given
        coEvery { api.getPopularTvSeries(any()) } returns getTvSeriesResponse()
        val popularSeriesSource = PopularSeriesSource(api = api)

        val expectedTvSeries = listOf(getSeries())
        val expectedResult = PagingSource.LoadResult.Page(
            data = expectedTvSeries, prevKey = null, nextKey = 2
        )

        // When
        val actualResult = popularSeriesSource.load(params)

        // Then
        assertEquals(expectedResult, actualResult)
    }


    private fun getSeries() = TvSeries(
        backdropPath = "/path/to/backdrop.jpg", firstAirDate = "2024-01-01", genreIds = listOf(18, 80), id = 12345, name = "The Example Series", originCountry = listOf("US"), originalLanguage = "en", originalName = "The Example Series", overview = "A gripping drama series that follows the life of a troubled detective as they solve complex crimes in a bustling city.", popularity = 8.7, posterPath = "/path/to/poster.jpg", voteAverage = 9.0, voteCount = 1500
    )

    private fun getTvSeriesResponse() = TvSeriesResponse(
        results = listOf(getSeries()), page = 1, totalResults = 10, totalPages = 5
    )
}