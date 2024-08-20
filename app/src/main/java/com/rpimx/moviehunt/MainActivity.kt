package com.rpimx.moviehunt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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
import com.rpimx.moviehunt.ui.theme.Theme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel = hiltViewModel<MainViewModel>()
            val themeValue by viewModel.theme.collectAsState(
                initial = Theme.LIGHT_THEME.themeValue, context = Dispatchers.Main.immediate
            )

            MovieHuntTheme(themeValue) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = DashboardScreenRoute.MainRoute) {
                    navigation<DashboardScreenRoute.MainRoute>(startDestination = DashboardScreenRoute.Home) {

                        composable<DashboardScreenRoute.Home> {
                            SharedTransitionLayout {
                                HomeScreen(navController = navController, animatedVisibilityScope = this@composable)
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
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieHuntTheme {
        //Greeting("Android")
    }
}