package com.rpimx.moviehunt.features.cast.domain.usecases

import com.rpimx.moviehunt.common.utils.Resource
import com.rpimx.moviehunt.features.cast.domain.model.Cast
import com.rpimx.moviehunt.features.cast.domain.model.Credits
import com.rpimx.moviehunt.features.cast.domain.repository.CastRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetMovieCastUseCaseTest {

    private lateinit var useCase: GetMovieCastUseCase
    private val repository: CastRepository = mockk()

    @Before
    fun setUp() {
        useCase = GetMovieCastUseCase(repository)
    }

    @Test
    fun `invoke should return movie casts successfully`() = runBlocking {
        // Arrange
        val movieId = 123
        val expectedCredits = Credits(
            cast = listOf(
                Cast(
                    adult = false, castId = 123, character = "Character 1", creditId = "creditId1", gender = 1, id = 456, knownForDepartment = "Acting", name = "Actor 1", order = 0, originalName = "Actor 1", popularity = 99.9, profilePath = "/path/to/image.jpg"
                )
            ), id = 789
        )
        coEvery { repository.getMovieCasts(movieId) } returns Resource.Success(expectedCredits)

        // Act
        val result = useCase.invoke(movieId)

        // Assert
        assertEquals(Resource.Success(expectedCredits), result)
    }

    @Test
    fun `invoke should return error when repository fails`() = runBlocking {
        // Arrange
        val movieId = 123
        val expectedError = Resource.Error<Credits>("Error message")
        coEvery { repository.getMovieCasts(movieId) } returns expectedError

        // Act
        val result = useCase.invoke(movieId)

        // Assert
        assertEquals(expectedError, result)
    }

}