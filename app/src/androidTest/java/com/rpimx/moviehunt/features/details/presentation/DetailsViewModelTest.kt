package com.rpimx.moviehunt.features.details.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rpimx.moviehunt.TestCoroutineRule
import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.common.utils.Resource
import com.rpimx.moviehunt.features.bookmark.data.local.Bookmark
import com.rpimx.moviehunt.features.bookmark.domain.repository.BookmarkRepository
import com.rpimx.moviehunt.features.cast.data.remote.dto.CastResponse
import com.rpimx.moviehunt.features.cast.domain.model.Cast
import com.rpimx.moviehunt.features.cast.domain.model.Credits
import com.rpimx.moviehunt.features.cast.domain.repository.CastRepository
import com.rpimx.moviehunt.features.cast.domain.usecases.GetMovieCastUseCase
import com.rpimx.moviehunt.features.cast.domain.usecases.GetTvCastUseCase
import com.rpimx.moviehunt.features.details.data.repository.DetailsRepository
import com.rpimx.moviehunt.features.home.data.remote.dto.CreditsResponse
import com.rpimx.moviehunt.features.home.data.remote.dto.MovieDetails
import com.rpimx.moviehunt.features.home.data.remote.dto.TvSeriesDetails
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@ExperimentalCoroutinesApi
class DetailsViewModelTest {


    @RelaxedMockK
    lateinit var detailsRepository: DetailsRepository

    @RelaxedMockK
    lateinit var castRepository: CastRepository

    @RelaxedMockK
    lateinit var bookmarkRepository: BookmarkRepository

    @RelaxedMockK
    lateinit var getTvCastUseCase: GetTvCastUseCase

    @RelaxedMockK
    lateinit var getMovieCastUseCase: GetMovieCastUseCase

    @RelaxedMockK
    private lateinit var viewModel: DetailsViewModel

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()


    @RelaxedMockK
    private lateinit var api: Api


    @Before
    fun setup() {
        hiltAndroidRule.inject()
        MockKAnnotations.init(this)
    }


    @Test
    fun getFilmDetails_should_fetch_movie_details_and_casts() = testCoroutineRule.runTest {
        // Arrange
        val movieId = 1
        val filmType = "movie"


        val mockMovieDetails = MovieDetails(
            adult = false,
            backdropPath = "/path/to/backdrop.jpg",
            budget = 150000000,
            homepage = "https://www.examplemovie.com",
            id = 1,
            imdbId = "tt1234567",
            originalLanguage = "en",
            originalTitle = "Example Movie",
            overview = "An exciting adventure of a young hero who overcomes great odds.",
            popularity = 8.7,
            posterPath = "/path/to/poster.jpg",
            releaseDate = "2024-08-15",
            revenue = 300000000,
            runtime = 120,
            status = "Released",
            tagline = "The adventure begins.",
            title = "Example Movie",
            video = false,
            voteAverage = 7.8,
            voteCount = 5000
        )

        val mockMovieCasts = Credits(
            cast = listOf(
                Cast(
                    adult = false, castId = 123, character = "Character 1", creditId = "creditId1", gender = 1, id = 456, knownForDepartment = "Acting", name = "Actor 1", order = 0, originalName = "Actor 1", popularity = 99.9, profilePath = "/path/to/image.jpg"
                )
            ), id = 789
        )

        coEvery { api.getMovieDetails(movieId) } returns mockMovieDetails
        coEvery { api.getMovieCredits(movieId) } returns CreditsResponse(
            id = 78910, cast = listOf(
                CastResponse(
                    adult = false, castId = 1, character = "Hero", creditId = "abc123", gender = 1, id = 101, knownForDepartment = "Acting", name = "John Doe", order = 1, originalName = "Johnathan Doe", popularity = 9.5, profilePath = "https://example.com/path/to/profile1.jpg"
                )
            )
        )
        coEvery { detailsRepository.getMoviesDetails(movieId) } returns Resource.Success(mockMovieDetails)
        coEvery { getMovieCastUseCase(movieId) } returns Resource.Success(mockMovieCasts)
        coEvery { castRepository.getMovieCasts(movieId) } returns Resource.Success(mockMovieCasts)


        // Act
        viewModel.getFilmDetails(movieId, filmType)

        launch {
            viewModel.detailsState.drop(3).collectLatest { data: DetailsState ->
                assertNotNull(data.movieDetails)
                assertNotNull(data.credits)
                assertFalse(data.isLoading)
                assertEquals(mockMovieDetails, data.movieDetails)
                assertEquals(mockMovieCasts, data.credits)
            }
        }
    }

    @Test
    fun getFilmDetails_should_fetch_TV_series_details_and_casts() = testCoroutineRule.runTest {
        // Arrange
        val tvId = 1
        val filmType = "tv"

        val mockTvSeriesDetails = TvSeriesDetails(
            adult = false,
            backdropPath = "/path/to/backdrop.jpg",
            episodeRunTime = listOf(45, 50), // example runtime in minutes
            firstAirDate = "2023-01-10",
            homepage = "https://www.exampletvseries.com",
            id = 1,
            inProduction = true,
            languages = listOf("en", "es"),
            lastAirDate = "2024-08-10",
            name = "Example TV Series",
            nextEpisodeToAir = "",
            numberOfEpisodes = 20,
            numberOfSeasons = 2,
            originCountry = listOf("US"),
            originalLanguage = "en",
            originalName = "Example TV Series",
            overview = "An intriguing drama that follows the lives of several characters intertwined in a complex plot.",
            popularity = 8.9,
            posterPath = "/path/to/poster.jpg",
            status = "Returning Series",
            tagline = "The story continues.",
            type = "Scripted",
            voteAverage = 8.2,
            voteCount = 3500
        )

        val mockTvSeriesCasts = Credits(
            cast = listOf(
                Cast(
                    adult = false, castId = 123, character = "Character 1", creditId = "creditId1", gender = 1, id = 456, knownForDepartment = "Acting", name = "Actor 1", order = 0, originalName = "Actor 1", popularity = 99.9, profilePath = "/path/to/image.jpg"
                )
            ), id = 789
        )

        coEvery { detailsRepository.getTvSeriesDetails(tvId) } returns Resource.Success(mockTvSeriesDetails)
        coEvery { getTvCastUseCase(tvId) } returns Resource.Success(mockTvSeriesCasts)
        coEvery { castRepository.getTvSeriesCasts(tvId) } returns Resource.Success(mockTvSeriesCasts)

        // Act
        viewModel.getFilmDetails(tvId, filmType)

        launch {
            viewModel.detailsState.drop(3).collectLatest { data: DetailsState ->
                // Assert
                assertNotNull(data)
                assertNotNull(data.tvSeriesDetails)
                assertNotNull(data.credits)
                assertFalse(data.isLoading)
            }
        }


    }

    @Test
    fun isBookmarked_should_return_true_if_item_is_bookmarked() = testCoroutineRule.runTest {
        // Arrange
        val mediaId = 1
        val bookmark = Bookmark(
            id = mediaId, title = "Movie 1", type = "Movie", image = "/path/to/image.jpg", rating = 4.5f, bookmark = true, overview = "Overview of Movie 1", releaseDate = "2023-01-01"
        )

        coEvery { bookmarkRepository.addToBookmark(bookmark) } returns Unit
        coEvery { bookmarkRepository.isBookmarked(mediaId) } returns flowOf(true)

        // Act
        viewModel.addToBookmark(bookmark)

        launch {
            viewModel.isBookmarked(mediaId).collectLatest { isBookmarked: Boolean ->
                // Assert
                assertTrue(isBookmarked)
                viewModel.deleteFromBookmark(bookmark)
            }
        }
    }


    @Test
    fun addToBookmark_should_add_item_to_bookmarks() = testCoroutineRule.runTest {
        // Arrange
        val bookmark = Bookmark(
            id = 1, title = "Movie 1", type = "Movie", image = "/path/to/image.jpg", rating = 4.5f, bookmark = true, overview = "Overview of Movie 1", releaseDate = "2023-01-01"
        )

        coEvery { bookmarkRepository.addToBookmark(bookmark) } returns Unit
        coEvery { bookmarkRepository.isBookmarked(bookmark.id) } returns flowOf(true)
        coEvery { bookmarkRepository.deleteFromBookmark(bookmark) } returns Unit

        // Act
        viewModel.addToBookmark(bookmark)

        launch {
            viewModel.isBookmarked(bookmark.id).collectLatest { isBookmarked: Boolean ->
                // Assert
                assertTrue(isBookmarked)
                viewModel.deleteFromBookmark(bookmark)
            }
        }
    }


    @Test
    fun deleteFromBookmark_should_remove_item_from_bookmarks() = testCoroutineRule.runTest {
        // Arrange
        val bookmark = Bookmark(
            id = 1, title = "Movie 1", type = "Movie", image = "/path/to/image.jpg", rating = 4.5f, bookmark = true, overview = "Overview of Movie 1", releaseDate = "2023-01-01"
        )

        coEvery { bookmarkRepository.addToBookmark(bookmark) } returns Unit
        coEvery { bookmarkRepository.deleteFromBookmark(bookmark) } returns Unit
        coEvery { bookmarkRepository.isBookmarked(bookmark.id) } returns flowOf(false)


        viewModel.addToBookmark(bookmark)

        // Act
        viewModel.deleteFromBookmark(bookmark)
        launch {
            viewModel.isBookmarked(bookmark.id).collectLatest { isBookmarked: Boolean ->
                // Assert
                assertFalse(isBookmarked)
            }
        }
    }
}
