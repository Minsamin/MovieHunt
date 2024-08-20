package com.rpimx.moviehunt.features.settings.presentation

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rpimx.moviehunt.CleanDataStoreTestRule
import com.rpimx.moviehunt.MainActivity
import com.rpimx.moviehunt.common.navigation.DashboardScreenRoute
import com.rpimx.moviehunt.ui.theme.MovieHuntTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@ExperimentalSharedTransitionApi
@ExperimentalFoundationApi
@ExperimentalCoroutinesApi
class SettingsScreenTest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val cleanDataStoreTestRule = CleanDataStoreTestRule()

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltAndroidRule.inject()
        composeTestRule.activity.setContent() {
            val navController = rememberNavController()
            MovieHuntTheme {
                NavHost(navController = navController, startDestination = DashboardScreenRoute.Settings) {
                    composable<DashboardScreenRoute.Settings> {
                        SettingsScreen(navController)
                    }
                }
            }
        }
    }


    @Test
    fun testHomeScreenUI() {
        with(composeTestRule) {
            onNodeWithText("Change Theme").assertIsDisplayed()
            onNodeWithText("Change Theme").assertIsDisplayed().performClick()
            onNodeWithText("Dark Mode").assertIsDisplayed()
            onNodeWithText("Dark Mode").assertIsDisplayed().performClick()
        }
    }
}
