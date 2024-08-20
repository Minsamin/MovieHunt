package com.rpimx.moviehunt.features.settings.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rpimx.moviehunt.TestCoroutineRule
import com.rpimx.moviehunt.common.domain.repository.PreferenceRepository
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @RelaxedMockK
    private lateinit var preferenceRepository: PreferenceRepository

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = SettingsViewModel(preferenceRepository)
    }

    @Test
    fun updateTheme_should_call_saveTheme_in_preferenceRepository() = testCoroutineRule.testRunBlocking {
        // Arrange
        val themeValue = 2

        // Act
        viewModel.updateTheme(themeValue)

        // Assert
        coVerify { preferenceRepository.saveTheme(themeValue) }
    }

    @Test
    fun theme_should_return_initial_value_from_repository() = testCoroutineRule.testRunBlocking {
        // Arrange
        val expectedThemeValue = 1
        every { preferenceRepository.getTheme() } returns flowOf(expectedThemeValue)
        val testViewModel = SettingsViewModel(preferenceRepository)

        // Act
        val actualThemeValue = testViewModel.theme.first()

        // Assert
        assertEquals(expectedThemeValue, actualThemeValue)
    }

    @Test
    fun theme_should_update_when_new_theme_value_is_set() = testCoroutineRule.testRunBlocking {
        // Arrange
        val newThemeValue = 2

        // Act
        viewModel.updateTheme(newThemeValue)

        // Assert
        coVerify { preferenceRepository.saveTheme(newThemeValue) }
    }
}