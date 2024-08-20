package com.rpimx.moviehunt.features.home.presentation

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rpimx.moviehunt.MainActivity
import com.rpimx.moviehunt.common.navigation.DashboardScreenRoute
import com.rpimx.moviehunt.ui.theme.MovieHuntTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@ExperimentalSharedTransitionApi
@ExperimentalFoundationApi
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        MockKAnnotations.init(this)
        composeTestRule.activity.setContent() {
            val navController = rememberNavController()
            MovieHuntTheme {
                NavHost(navController = navController, startDestination = DashboardScreenRoute.Home) {
                    composable<DashboardScreenRoute.Home> {
                        SharedTransitionLayout {
                            HomeScreen(
                                navController = navController,
                                animatedVisibilityScope = this@composable,
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun testHomeScreenUI() {
        with(composeTestRule) {
            waitForIdle()
            onNodeWithTag("Movies").assertIsDisplayed()
            waitForIdle()
            onNodeWithTag("Shows").assertIsDisplayed().performClick()
        }
    }
}