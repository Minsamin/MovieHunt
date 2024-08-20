package com.rpimx.moviehunt.features.search.presentation

import android.app.appsearch.SearchResult
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.rpimx.moviehunt.TestCoroutineRule
import com.rpimx.moviehunt.TestDiffCallback
import com.rpimx.moviehunt.TestListCallback
import com.rpimx.moviehunt.features.search.domain.model.search.Search
import com.rpimx.moviehunt.features.search.domain.usecase.SearchUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @MockK
    private lateinit var searchUseCase: SearchUseCase

    @MockK

    private lateinit var viewModel: SearchViewModel
    private lateinit var differForSearch: AsyncPagingDataDiffer<Search>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SearchViewModel(searchUseCase)
        differForSearch = AsyncPagingDataDiffer(
            diffCallback = TestDiffCallback(), updateCallback = TestListCallback(), workerDispatcher = Dispatchers.Main
        )
    }

    @Test
    fun searchAll_should_update_searchState_with_results() = testCoroutineRule.testRunBlocking {
        // Arrange
        val searchParam = "Iron Man"
        every { searchUseCase(searchParam) } returns flowOf(PagingData.from(getSearchList()))


        // Act
        viewModel.searchAll(searchParam)

        val job = launch {
            viewModel.searchState.value.searchResult.collectLatest {
                differForSearch.submitData(it)
            }
        }
        advanceUntilIdle()

        // Then
        val resultList: List<Search> = differForSearch.snapshot().items
        val mockedList: List<Search> = getSearchList()
        assertEquals(mockedList, resultList)
        job.cancel()
    }

    @Test
    fun updateSearchTerm_should_update_searchTerm_in_state() = testCoroutineRule.testRunBlocking {
        // Arrange
        val searchTerm = "new_term"

        // Act
        viewModel.updateSearchTerm(searchTerm)

        // Assert
        val result = viewModel.searchState.first().searchTerm
        assertEquals(searchTerm, result)
    }

    @Test
    fun clearSearch_should_reset_searchState() = testCoroutineRule.testRunBlocking {
        // Act
        viewModel.clearSearch()

        // Assert
        val state = viewModel.searchState.first()
        assertEquals("", state.searchTerm)
        assertEquals(emptyFlow<SearchResult>(), state.searchResult)
    }


    private fun getSearchList() = listOf(
        Search(
            adult = false, backdropPath = "/path/to/backdrop.jpg", firstAirDate = null, genreIds = listOf(18), id = 202020, mediaType = "movie", name = null, originCountry = listOf("US"), originalLanguage = "en", originalName = null, originalTitle = "Iron Man 1", overview = "An amazing movie.", popularity = 9.1, posterPath = "/path/to/poster.jpg", releaseDate = "2024-03-15", title = "Iron Man Movie", video = false, voteAverage = 8.7, voteCount = 3500
        ), Search(
            adult = false, backdropPath = "/path/to/backdrop2.jpg", firstAirDate = "2024-01-15", genreIds = listOf(18, 80), id = 101010, mediaType = "tv", name = "Dark", originCountry = listOf("US"), originalLanguage = "en", originalName = "Dark TV Show", originalTitle = null, overview = "A great TV show.", popularity = 8.7, posterPath = "/path/to/poster2.jpg", releaseDate = null, title = null, video = false, voteAverage = 8.5, voteCount = 2000
        ), Search(
            adult = false, backdropPath = "/path/to/backdrop2.jpg", firstAirDate = "2024-01-15", genreIds = listOf(18, 80), id = 101010, mediaType = "tv", name = "Avenger", originCountry = listOf("US"), originalLanguage = "en", originalName = "Avenger Movie", originalTitle = null, overview = "A great TV show.", popularity = 8.7, posterPath = "/path/to/poster2.jpg", releaseDate = null, title = null, video = false, voteAverage = 8.5, voteCount = 2000
        )
    )

}