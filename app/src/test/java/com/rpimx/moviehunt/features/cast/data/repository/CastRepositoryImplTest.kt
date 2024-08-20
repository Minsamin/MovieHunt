package com.rpimx.moviehunt.features.cast.data.repository

import com.rpimx.moviehunt.common.data.remote.Api
import com.rpimx.moviehunt.common.utils.Resource
import com.rpimx.moviehunt.features.cast.data.remote.dto.CastResponse
import com.rpimx.moviehunt.features.details.data.mapper.toDomain
import com.rpimx.moviehunt.features.home.data.remote.dto.CreditsResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CastRepositoryImplTest {

    @MockK
    private lateinit var api: Api

    private lateinit var repository: CastRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = CastRepositoryImpl(api)
    }

    @Test
    fun test_get_movie_casts_success() = runTest {
        // Given
        val mockCredits = getResponse().toDomain()
        coEvery { api.getMovieCredits(1) } returns getResponse()

        // When
        val response = repository.getMovieCasts(1)

        // Then
        assertTrue(response is Resource.Success)
        assertEquals(mockCredits.id, (response as Resource.Success).data?.id)
    }


    @Test
    fun test_get_movie_casts_failure() = runTest {
        // Given
        val mockError = "Something went wrong!"
        coEvery { api.getMovieCredits(1) } throws NullPointerException(mockError)

        // When
        val response = repository.getMovieCasts(1)

        // Then
        assertTrue(response is Resource.Error)
        assertEquals(mockError, (response as Resource.Error).message)
        assertNull(response.data)
    }


    @Test
    fun test_get_tv_casts_success() = runTest {
        // Given
        val mockCreditsResponse = getResponse()
        coEvery { api.getTvSeriesCredits(1) } returns mockCreditsResponse

        // When
        val response = repository.getTvSeriesCasts(1)

        // Then
        val expected = Resource.Success(mockCreditsResponse.toDomain())
        assertEquals(expected.data?.id, response.data?.id)
        assertEquals(expected, response)
    }


    @Test
    fun test_get_tv_casts_failure() = runTest {
        // Given
        val mockError = "Something went wrong!"
        coEvery { api.getTvSeriesCredits(1) } throws NullPointerException(mockError)

        // When
        val response = repository.getTvSeriesCasts(1)

        // Then
        assertTrue(response is Resource.Error)
        assertEquals(mockError, (response as Resource.Error).message)
        assertNull(response.data)
    }


    private fun getResponse() = CreditsResponse(
        id = 78910, cast = listOf(
            CastResponse(
                adult = false, castId = 1, character = "Hero", creditId = "abc123", gender = 1, id = 101, knownForDepartment = "Acting", name = "John Doe", order = 1, originalName = "Johnathan Doe", popularity = 9.5, profilePath = "https://example.com/path/to/profile1.jpg"
            )
        )
    )

}