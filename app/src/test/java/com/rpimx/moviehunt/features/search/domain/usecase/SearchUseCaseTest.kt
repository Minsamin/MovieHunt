package com.rpimx.moviehunt.features.search.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.rpimx.moviehunt.TestCoroutineRule
import com.rpimx.moviehunt.features.search.domain.model.search.Search
import com.rpimx.moviehunt.features.search.domain.repository.SearchRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val mockRepository: SearchRepository = mockk()
    private lateinit var searchUseCase: SearchUseCase


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        searchUseCase = SearchUseCase(mockRepository)
    }

    @Test
    fun `test invoke filters paging data correctly`() = testCoroutineRule.testRunBlocking {
        // Arrange
        val searchParam = "example"

        val pagingData = PagingData.from(
            listOf(
                Search(
                    adult = false, backdropPath = "/path/to/backdrop.jpg", firstAirDate = "2024-01-15", genreIds = listOf(18, 80), id = 101010, mediaType = "tv", name = "Valid TV Show", originCountry = listOf("US"), originalLanguage = "en", originalName = "Valid TV Show", originalTitle = null, overview = "A great TV show.", popularity = 8.7, posterPath = "/path/to/poster.jpg", releaseDate = null, title = null, video = false, voteAverage = 8.5, voteCount = 2000
                ), Search(
                    adult = false, backdropPath = "/path/to/backdrop.jpg", firstAirDate = null, genreIds = listOf(18), id = 202020, mediaType = "movie", name = null, originCountry = listOf("US"), originalLanguage = "en", originalName = null, originalTitle = "Valid Movie Title", overview = "An amazing movie.", popularity = 9.1, posterPath = "/path/to/poster.jpg", releaseDate = "2024-03-15", title = "Valid Movie Title", video = false, voteAverage = 8.7, voteCount = 3500
                ), Search(
                    adult = false, backdropPath = "/path/to/backdrop.jpg", firstAirDate = "2024-02-01", genreIds = listOf(28, 12), id = 303030, mediaType = "movie", name = null, originCountry = listOf("FR"), originalLanguage = "fr", originalName = null, originalTitle = null, overview = "Another movie.", popularity = 6.5, posterPath = "/path/to/poster.jpg", releaseDate = "2024-04-01", title = null, video = false, voteAverage = 7.0, voteCount = 1200
                )
            )
        )

        coEvery { mockRepository.searchMovieOrTv(searchParam) } returns flowOf(pagingData)

        // When
        val resultFlow: Flow<PagingData<Search>> = searchUseCase(searchParam)
        val actualResults: List<Search> = resultFlow.asSnapshot()


        // Expected data after filtering
        val expectedList = listOf(
            Search(
                adult = false, backdropPath = "/path/to/backdrop.jpg", firstAirDate = "2024-01-15", genreIds = listOf(18, 80), id = 101010, mediaType = "tv", name = "Valid TV Show", originCountry = listOf("US"), originalLanguage = "en", originalName = "Valid TV Show", originalTitle = null, overview = "A great TV show.", popularity = 8.7, posterPath = "/path/to/poster.jpg", releaseDate = null, title = null, video = false, voteAverage = 8.5, voteCount = 2000
            ), Search(
                adult = false, backdropPath = "/path/to/backdrop.jpg", firstAirDate = null, genreIds = listOf(18), id = 202020, mediaType = "movie", name = null, originCountry = listOf("US"), originalLanguage = "en", originalName = null, originalTitle = "Valid Movie Title", overview = "An amazing movie.", popularity = 9.1, posterPath = "/path/to/poster.jpg", releaseDate = "2024-03-15", title = "Valid Movie Title", video = false, voteAverage = 8.7, voteCount = 3500
            )
        )

        // Assert
        assertEquals(expectedList, actualResults)
    }
}
