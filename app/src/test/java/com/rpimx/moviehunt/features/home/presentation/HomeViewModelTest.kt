package com.rpimx.moviehunt.features.home.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.rpimx.moviehunt.TestCoroutineRule
import com.rpimx.moviehunt.TestDiffCallback
import com.rpimx.moviehunt.TestListCallback
import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.features.home.data.remote.dto.MoviesResponse
import com.rpimx.moviehunt.features.home.data.remote.dto.TvSeriesResponse
import com.rpimx.moviehunt.features.home.data.repository.MoviesRepository
import com.rpimx.moviehunt.features.home.data.repository.TvSeriesRepository
import com.rpimx.moviehunt.features.home.domain.models.Movie
import com.rpimx.moviehunt.features.home.domain.models.TvSeries
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule(order = 1)
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: HomeViewModel
    private val moviesRepository: MoviesRepository = mockk()
    private val seriesRepository: TvSeriesRepository = mockk()

    @MockK
    private lateinit var api: Api

    private lateinit var mockPagingDataForMovie: Flow<PagingData<Movie>>
    private lateinit var mockPagingDataForTV: Flow<PagingData<TvSeries>>

    private lateinit var differForMovies: AsyncPagingDataDiffer<Movie>
    private lateinit var differForTvSeries: AsyncPagingDataDiffer<TvSeries>

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        mockPagingDataForMovie = flowOf(PagingData.from(listOf(getMovie())))
        mockPagingDataForTV = flowOf(PagingData.from(listOf(getSeries())))

        coEvery { api.getTrendingTodayMovies(any()) } returns getMovieResponse()
        coEvery { api.getTopRatedTvSeries(any()) } returns getTvSeriesResponse()

        // Mock methods for MoviesRepository
        every { moviesRepository.getTrendingMoviesThisWeek() } returns mockPagingDataForMovie
        every { moviesRepository.getUpcomingMovies() } returns mockPagingDataForMovie
        every { moviesRepository.getTopRatedMovies() } returns mockPagingDataForMovie
        every { moviesRepository.getNowPlayingMovies() } returns mockPagingDataForMovie
        every { moviesRepository.getPopularMovies() } returns mockPagingDataForMovie

        // Mock methods for TvSeriesRepository
        every { seriesRepository.getTrendingThisWeekTvSeries() } returns mockPagingDataForTV
        every { seriesRepository.getOnTheAirTvSeries() } returns mockPagingDataForTV
        every { seriesRepository.getTopRatedTvSeries() } returns mockPagingDataForTV
        every { seriesRepository.getAiringTodayTvSeries() } returns mockPagingDataForTV
        every { seriesRepository.getPopularTvSeries() } returns mockPagingDataForTV


        viewModel = HomeViewModel(moviesRepository, seriesRepository)

        differForMovies = AsyncPagingDataDiffer(
            diffCallback = TestDiffCallback(), updateCallback = TestListCallback(), workerDispatcher = Dispatchers.Main
        )
        differForTvSeries = AsyncPagingDataDiffer(
            diffCallback = TestDiffCallback(), updateCallback = TestListCallback(), workerDispatcher = Dispatchers.Main
        )
    }

    @Test
    fun `init should call refreshAllData`() = runTest {
        coVerify {
            moviesRepository.getTrendingMoviesThisWeek()
            moviesRepository.getNowPlayingMovies()
            moviesRepository.getUpcomingMovies()
            moviesRepository.getTopRatedMovies()
            moviesRepository.getPopularMovies()
            seriesRepository.getPopularTvSeries()
            seriesRepository.getAiringTodayTvSeries()
            seriesRepository.getTrendingThisWeekTvSeries()
            seriesRepository.getOnTheAirTvSeries()
            seriesRepository.getTopRatedTvSeries()
        }
    }

    @Test
    fun `getTrendingMovies should update trendingMovies state`() = testCoroutineRule.runTest {
        // Given
        every { moviesRepository.getTrendingMoviesThisWeek() } returns mockPagingDataForMovie

        // When
        viewModel.getTrendingMovies()
        val job = launch {
            viewModel.homeState.value.trendingMovies.collectLatest {
                differForMovies.submitData(it)
            }
        }
        advanceUntilIdle()

        // Then
        val resultList: List<Movie> = differForMovies.snapshot().items
        val mockedList: List<Movie> = listOf(getMovie())
        assertEquals(mockedList, resultList)
        job.cancel()
    }

    @Test
    fun `getUpcomingMovies should update upcomingMovies state`() = testCoroutineRule.runTest {
        // Given
        every { moviesRepository.getUpcomingMovies() } returns mockPagingDataForMovie

        // When
        viewModel.getUpcomingMovies()
        val job = launch {
            viewModel.homeState.value.upcomingMovies.collectLatest {
                differForMovies.submitData(it)
            }
        }
        advanceUntilIdle()

        // Then
        val resultList: List<Movie> = differForMovies.snapshot().items
        val mockedList: List<Movie> = listOf(getMovie())
        assertEquals(mockedList, resultList)
        job.cancel()
    }

    @Test
    fun `getTopRatedMovies should update topRatedMovies state`() = testCoroutineRule.runTest {
        // Given
        every { moviesRepository.getTopRatedMovies() } returns mockPagingDataForMovie

        // When
        viewModel.getTopRatedMovies()
        val job = launch {
            viewModel.homeState.value.topRatedMovies.collectLatest {
                differForMovies.submitData(it)
            }
        }
        advanceUntilIdle()

        // Then
        val resultList: List<Movie> = differForMovies.snapshot().items
        val mockedList: List<Movie> = listOf(getMovie())
        assertEquals(mockedList, resultList)
        job.cancel()
    }

    @Test
    fun `getNowPlayingMovies should update nowPlayingMovies state`() = testCoroutineRule.runTest {
        // Given
        every { moviesRepository.getNowPlayingMovies() } returns mockPagingDataForMovie

        // When
        viewModel.getNowPayingMovies()
        val job = launch {
            viewModel.homeState.value.nowPlayingMovies.collectLatest {
                differForMovies.submitData(it)
            }
        }
        advanceUntilIdle()

        // Then
        val resultList: List<Movie> = differForMovies.snapshot().items
        val mockedList: List<Movie> = listOf(getMovie())
        assertEquals(mockedList, resultList)
        job.cancel()
    }

    @Test
    fun `getPopularMovies should update popularMovies state`() = testCoroutineRule.runTest {
        // Given
        every { moviesRepository.getPopularMovies() } returns mockPagingDataForMovie

        // When
        viewModel.getPopularMovies()
        val job = launch {
            viewModel.homeState.value.popularMovies.collectLatest {
                differForMovies.submitData(it)
            }
        }
        advanceUntilIdle()

        // Then
        val resultList: List<Movie> = differForMovies.snapshot().items
        val mockedList: List<Movie> = listOf(getMovie())
        assertEquals(mockedList, resultList)
        job.cancel()
    }

    @Test
    fun `getTrendingTvSeries should update trendingTvSeries state`() = testCoroutineRule.runTest {
        // Given
        every { seriesRepository.getTrendingThisWeekTvSeries() } returns mockPagingDataForTV

        // When
        viewModel.getTrendingTvSeries()
        val job = launch {
            viewModel.homeState.value.trendingTvSeries.collectLatest {
                differForTvSeries.submitData(it)
            }
        }
        advanceUntilIdle()

        // Then
        val resultList: List<TvSeries> = differForTvSeries.snapshot().items
        val mockedList: List<TvSeries> = listOf(getSeries())
        assertEquals(mockedList, resultList)
        job.cancel()
    }

    @Test
    fun `getOnTheAirTvSeries should update onAirTvSeries state`() = testCoroutineRule.runTest {
        // Given
        every { seriesRepository.getOnTheAirTvSeries() } returns mockPagingDataForTV

        // When
        viewModel.getOnTheAirTvSeries()
        val job = launch {
            viewModel.homeState.value.onAirTvSeries.collectLatest {
                differForTvSeries.submitData(it)
            }
        }
        advanceUntilIdle()

        // Then
        val resultList: List<TvSeries> = differForTvSeries.snapshot().items
        val mockedList: List<TvSeries> = listOf(getSeries())
        assertEquals(mockedList, resultList)
        job.cancel()
    }

    @Test
    fun `getTopRatedTvSeries should update topRatedTvSeries state`() = testCoroutineRule.runTest {
        // Given
        every { seriesRepository.getTopRatedTvSeries() } returns mockPagingDataForTV

        // When
        viewModel.getTopRatedTvSeries()
        val job = launch {
            viewModel.homeState.value.topRatedTvSeries.collectLatest {
                differForTvSeries.submitData(it)
            }
        }
        advanceUntilIdle()

        // Then
        val resultList: List<TvSeries> = differForTvSeries.snapshot().items
        val mockedList: List<TvSeries> = listOf(getSeries())
        assertEquals(mockedList, resultList)
        job.cancel()
    }

    @Test
    fun `getAiringTodayTvSeries should update airingTodayTvSeries state`() = testCoroutineRule.runTest {
        // Given

        coEvery { seriesRepository.getAiringTodayTvSeries() } returns mockPagingDataForTV

        // When
        viewModel.getAiringTodayTvSeries()
        val job = launch {
            viewModel.homeState.value.airingTodayTvSeries.collectLatest {
                differForTvSeries.submitData(it)
            }
        }
        advanceUntilIdle()

        // Then
        val resultList: List<TvSeries> = differForTvSeries.snapshot().items
        val mockedList: List<TvSeries> = listOf(getSeries())
        assertEquals(mockedList, resultList)
        job.cancel()
    }

    @Test
    fun `setSelectedOption should update selectedFilmOption state`() = testCoroutineRule.runTest {
        // When
        viewModel.setSelectedOption("New Option")

        // Then
        assertEquals("New Option", viewModel.homeState.value.selectedFilmOption)
    }

    private fun getMovie() = Movie(
        adult = false, backdropPath = "/path/to/backdrop.jpg", genreIds = listOf(28, 12, 14), id = 123456, originalLanguage = "en", originalTitle = "The Example Movie", overview = "An exciting adventure of a hero on a quest to save the world. Full of thrilling moments and spectacular visuals.", popularity = 7.8, posterPath = "/path/to/poster.jpg", releaseDate = "2024-05-10", title = "The Example Movie", video = false, voteAverage = 8.5, voteCount = 4500
    )

    private fun getMovieResponse() = MoviesResponse(
        searches = listOf(getMovie()), page = 1, totalResults = 10, totalPages = 5
    )

    private fun getSeries() = TvSeries(
        backdropPath = "/path/to/backdrop.jpg", firstAirDate = "2024-01-01", genreIds = listOf(18, 80), id = 12345, name = "The Example Series", originCountry = listOf("US"), originalLanguage = "en", originalName = "The Example Series", overview = "A gripping drama series that follows the life of a troubled detective as they solve complex crimes in a bustling city.", popularity = 8.7, posterPath = "/path/to/poster.jpg", voteAverage = 9.0, voteCount = 1500
    )

    private fun getTvSeriesResponse() = TvSeriesResponse(
        results = listOf(getSeries()), page = 1, totalResults = 10, totalPages = 5
    )
}