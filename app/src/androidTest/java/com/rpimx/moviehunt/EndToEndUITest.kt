package com.rpimx.moviehunt

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.test.espresso.Espresso
import com.rpimx.moviehunt.common.domain.model.Film
import com.rpimx.moviehunt.common.navigation.DashboardScreenRoute
import com.rpimx.moviehunt.common.navigation.SealedClassNavType
import com.rpimx.moviehunt.features.bookmark.presentation.BookmarkScreensScreen
import com.rpimx.moviehunt.features.cast.domain.model.Credits
import com.rpimx.moviehunt.features.cast.presentation.CastsOverviewScreen
import com.rpimx.moviehunt.features.details.presentation.DetailsScreen
import com.rpimx.moviehunt.features.home.presentation.HomeScreen
import com.rpimx.moviehunt.features.search.presentation.SearchScreen
import com.rpimx.moviehunt.features.settings.presentation.SettingsScreen
import com.rpimx.moviehunt.ui.theme.MovieHuntTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.typeOf

@HiltAndroidTest
@ExperimentalSharedTransitionApi
@ExperimentalFoundationApi
class EndToEndUITest {

    @get:Rule(order = 0)
    val hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val cleanDataStoreTestRule = CleanDataStoreTestRule()

    @get:Rule(order = 2)
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

                    composable<DashboardScreenRoute.DetailsScreenArguments>(
                        typeMap = mapOf(
                            typeOf<Film>() to SealedClassNavType(Film.serializer()) as NavType<Film>,
                        )
                    ) {
                        val args = it.toRoute<DashboardScreenRoute.DetailsScreenArguments>()
                        SharedTransitionLayout {
                            DetailsScreen(
                                navController = navController, film = args.film, animatedVisibilityScope = this@composable
                            )
                        }
                    }
                    composable<DashboardScreenRoute.CastsOverviewScreenArguments>(
                        typeMap = mapOf(
                            typeOf<Credits>() to SealedClassNavType(Credits.serializer()) as NavType<Credits>,
                        )
                    ) {
                        val args = it.toRoute<DashboardScreenRoute.CastsOverviewScreenArguments>()
                        CastsOverviewScreen(
                            navController = navController, credits = args.credits
                        )
                    }
                    composable<DashboardScreenRoute.Search> {
                        SearchScreen(navController)
                    }
                    composable<DashboardScreenRoute.Bookmark> {
                        BookmarkScreensScreen(navController)
                    }
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

            // Go through the Bottom navigation
            waitForIdle()
            onNodeWithTag(DashboardScreenRoute.Search.routeName).assertIsDisplayed().performClick()
            waitForIdle()
            onNodeWithTag(DashboardScreenRoute.Bookmark.routeName).assertIsDisplayed().performClick()
            waitForIdle()

            Espresso.pressBack() // Close Keyboard
            waitForIdle()

            onNodeWithTag(DashboardScreenRoute.Settings.routeName).assertIsDisplayed().performClick()
            waitForIdle()
            onNodeWithTag(DashboardScreenRoute.Home.routeName).assertIsDisplayed().performClick()
            waitForIdle()


            // Wait for the network call to complete and the items to be loaded
            // Select a movie and Add to bookmark
            waitUntil(timeoutMillis = 5000) {
                onAllNodes(hasClickAction()).fetchSemanticsNodes().isNotEmpty()
            }
            waitForIdle()
            onAllNodes(hasClickAction())[5].performClick()
            waitForIdle()
            onNodeWithTag("DetailsScreen").assertIsDisplayed()
            waitForIdle()
            waitUntil(timeoutMillis = 5000) { onNodeWithTag("AddToBookMark").isDisplayed() }
            onNodeWithTag("AddToBookMark").assertIsDisplayed().performClick()
            waitForIdle()


            // Verify Movie added to bookmark section
            Espresso.pressBack() //go back to Home Screen
            waitForIdle()
            onNodeWithTag(DashboardScreenRoute.Bookmark.routeName).assertIsDisplayed().performClick()
            waitForIdle()

            composeTestRule.waitUntil(timeoutMillis = 5000) {
                composeTestRule.onAllNodesWithTag("BookmarkGrid").fetchSemanticsNodes().isNotEmpty()
            }

            composeTestRule.onNodeWithTag("BookmarkGrid").onChildren().assertCountEquals(1)


            // Search for a movie ans select It
            waitForIdle()
            onNodeWithTag(DashboardScreenRoute.Search.routeName).assertIsDisplayed().performClick()

            waitForIdle()
            composeTestRule.onNodeWithTag("SearchBar").performTextInput("Iron Man")

            waitForIdle()
            Espresso.pressBack() // Close Keyboard
            waitForIdle()


            waitUntil(timeoutMillis = 5000) {
                onAllNodes(hasClickAction()).fetchSemanticsNodes().isNotEmpty()
            }
            waitForIdle()

            composeTestRule.onNodeWithTag("SearchList").onChildAt(2).performClick()
            waitForIdle()


            // Add to bookmark
            onNodeWithTag("DetailsScreen").assertIsDisplayed()
            waitForIdle()
            waitUntil(timeoutMillis = 5000) { onNodeWithTag("AddToBookMark").isDisplayed() }
            onNodeWithTag("AddToBookMark").assertIsDisplayed().performClick()
            waitForIdle()


            // Verify total 2 movies added into bookmark section
            Espresso.pressBack() //go back to Home Screen
            waitForIdle()
            onNodeWithTag(DashboardScreenRoute.Bookmark.routeName).assertIsDisplayed().performClick()
            waitForIdle()

            composeTestRule.waitUntil(timeoutMillis = 5000) {
                composeTestRule.onAllNodesWithTag("BookmarkGrid").fetchSemanticsNodes().isNotEmpty()
            }

            composeTestRule.onNodeWithTag("BookmarkGrid").onChildren().assertCountEquals(2)


            waitForIdle()
            onNodeWithTag(DashboardScreenRoute.Home.routeName).assertIsDisplayed().performClick()
            waitForIdle()
            // END TEST
        }
    }
}