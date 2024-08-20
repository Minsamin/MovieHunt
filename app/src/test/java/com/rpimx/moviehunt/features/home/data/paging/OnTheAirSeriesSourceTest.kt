package com.rpimx.moviehunt.features.home.data.paging

import androidx.paging.PagingSource
import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.features.home.data.remote.dto.TvSeriesResponse
import com.rpimx.moviehunt.features.home.domain.models.TvSeries
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class OnTheAirSeriesSourceTest {

    @MockK
    private lateinit var api: Api

    private lateinit var source: OnTheAirSeriesSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        source = OnTheAirSeriesSource(api)
    }

    @Test
    fun `test load returns correct LoadResult`() = runTest {
        // Given
        val mockResponse = TvSeriesResponse(results = getData(), page = 1, totalResults = 10, totalPages = 5)
        coEvery { api.getOnTheAirTvSeries(1) } returns mockResponse

        // When
        val result = source.load(PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false))

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page
        assertEquals(mockResponse.results, pageResult.data)
        assertEquals(null, pageResult.prevKey)
        assertEquals(2, pageResult.nextKey)
    }


    @Test
    fun `test load handles API exceptions`() = runTest {
        // Given
        coEvery { api.getOnTheAirTvSeries(1) } throws IOException("Network error")

        // When
        val result = source.load(PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false))

        // Then
        assertTrue(result is PagingSource.LoadResult.Error)
        val errorResult = result as PagingSource.LoadResult.Error
        assertTrue(errorResult.throwable is IOException)
        assertEquals("Network error", errorResult.throwable.message)
    }


    private fun getData() = listOf(
        TvSeries(
            backdropPath = "/path/to/backdrop.jpg", firstAirDate = "2024-01-15", genreIds = listOf(18, 80), id = 101010, name = "Example TV Series", originCountry = listOf("US"), originalLanguage = "en", originalName = "Example TV Series", overview = "An intriguing series that dives into the lives of individuals embroiled in a complex and thrilling narrative.", popularity = 8.7, posterPath = "/path/to/poster.jpg", voteAverage = 8.5, voteCount = 2000
        )
    )
}