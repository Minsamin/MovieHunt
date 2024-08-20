package com.rpimx.moviehunt.features.home.data.paging

import androidx.paging.PagingSource
import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.features.home.data.remote.dto.MoviesResponse
import com.rpimx.moviehunt.features.home.domain.models.Movie
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class NowPlayingMoviesSourceTest {

    @MockK
    private lateinit var api: Api

    private lateinit var source: NowPlayingMoviesSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        source = NowPlayingMoviesSource(api)
    }

    @Test
    fun `test load returns correct LoadResult`() = runTest {
        // Given
        val mockResponse = MoviesResponse(searches = getData(), page = 1, totalResults = 10, totalPages = 5)
        coEvery { api.getNowPlayingMovies(1) } returns mockResponse

        // When
        val result = source.load(PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false))

        // Then
        val expectedResult = PagingSource.LoadResult.Page(
            data = getData(), prevKey = null, nextKey = 2
        )
        assertTrue(result is PagingSource.LoadResult.Page)
        val pageResult = result as PagingSource.LoadResult.Page
        assertEquals(expectedResult.data, pageResult.data)
        assertEquals(expectedResult.prevKey, pageResult.prevKey)
        assertEquals(expectedResult.nextKey, pageResult.nextKey)
    }


    @Test
    fun `test load handles API exceptions`() = runTest {
        // Given
        coEvery { api.getNowPlayingMovies(1) } throws IOException("Network error")

        // When
        val result = source.load(PagingSource.LoadParams.Refresh(key = null, loadSize = 20, placeholdersEnabled = false))

        // Then
        assertTrue(result is PagingSource.LoadResult.Error)
        val errorResult = result as PagingSource.LoadResult.Error
        assertTrue(errorResult.throwable is IOException)
        assertEquals("Network error", errorResult.throwable.message)
    }


    private fun getData() = listOf(
        Movie(
            adult = false, backdropPath = "/path/to/backdrop.jpg", genreIds = listOf(28, 12, 14),
            id = 123456, originalLanguage = "en", originalTitle = "The Example Movie", overview = "An exciting adventure of a hero on a quest to save the world. Full of thrilling moments and spectacular visuals.", popularity = 7.8, posterPath = "/path/to/poster.jpg", releaseDate = "2024-05-10", title = "The Example Movie", video = false, voteAverage = 8.5, voteCount = 4500
        )
    )
}