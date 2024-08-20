package com.rpimx.moviehunt.features.details.data.repository

import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.common.utils.Resource
import com.rpimx.moviehunt.features.home.data.remote.dto.MovieDetails
import com.rpimx.moviehunt.features.home.data.remote.dto.TvSeriesDetails
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class DetailsRepositoryTest {

    @MockK
    private lateinit var api: Api

    private lateinit var repository: DetailsRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = DetailsRepository(api)
    }

    @Test
    fun test_get_movie_details_success() = runTest {
        // Given
        coEvery { api.getMovieDetails(1) } returns getMovieResponse()

        // When
        val response = repository.getMoviesDetails(1)

        // Then
        assertTrue(response is Resource.Success)
        assertEquals(getMovieResponse().id, (response as Resource.Success).data?.id)
    }


    @Test
    fun test_get_movie_details_failure() = runTest {
        // Given
        val mockError = "Something went wrong!"
        coEvery { api.getMovieDetails(1) } throws NullPointerException(mockError)

        // When
        val response = repository.getMoviesDetails(1)

        // Then
        assertTrue(response is Resource.Error)
        assertEquals(mockError, (response as Resource.Error).message)
        assertNull(response.data)
    }


    @Test
    fun test_get_tv_details_success() = runTest {
        // Given
        coEvery { api.getTvSeriesDetails(1) } returns getTvResponse()

        // When
        val response = repository.getTvSeriesDetails(1)

        // Then
        assertTrue(response is Resource.Success)
        assertEquals(getTvResponse().id, (response as Resource.Success).data?.id)
    }


    @Test
    fun test_get_tv_details_failure() = runTest {
        // Given
        val mockError = "Something went wrong!"
        coEvery { api.getTvSeriesDetails(1) } throws NullPointerException(mockError)

        // When
        val response = repository.getTvSeriesDetails(1)

        // Then
        assertTrue(response is Resource.Error)
        assertEquals(mockError, (response as Resource.Error).message)
        assertNull(response.data)
    }


    private fun getMovieResponse() = MovieDetails(
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


    private fun getTvResponse() = TvSeriesDetails(
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


}