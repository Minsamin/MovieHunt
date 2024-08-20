package com.rpimx.moviehunt.features.home.presentation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rpimx.moviehunt.R
import com.rpimx.moviehunt.common.navigation.BottomBar
import com.rpimx.moviehunt.common.navigation.DashboardScreenRoute
import com.rpimx.moviehunt.common.utils.getImageUrl
import com.rpimx.moviehunt.features.home.domain.models.toFilm
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    animatedVisibilityScope: AnimatedContentScope,
    navController: NavController, viewModel: HomeViewModel = hiltViewModel()
) {
    val homeUiState by viewModel.homeState.collectAsState()

    Scaffold(bottomBar = {
        BottomBar(navController)
    }) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            HomeScreenContent(state = homeUiState, animatedVisibilityScope = animatedVisibilityScope, onEvent = { event ->
                when (event) {
                    is HomeEvents.NavigateBack -> {
                        navController.navigateUp()
                    }

                    is HomeEvents.NavigateToDetails -> {
                        navController.navigate(
                            DashboardScreenRoute.DetailsScreenArguments(film = event.film)
                        )
                    }

                    is HomeEvents.OnFilmOptionSelected -> viewModel.setSelectedOption(event.item)
                    HomeEvents.OnPullToRefresh -> viewModel.refreshAllData()
                }
            })
        }

    }
}


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterialApi::class)
@Composable
fun SharedTransitionScope.HomeScreenContent(
    state: HomeState, animatedVisibilityScope: AnimatedContentScope, onEvent: (HomeEvents) -> Unit
) {
    val context = LocalContext.current
    val trendingMovies = state.trendingMovies.collectAsLazyPagingItems()
    val upcomingMovies = state.upcomingMovies.collectAsLazyPagingItems()
    val topRatedMovies = state.topRatedMovies.collectAsLazyPagingItems()
    val nowPlayingMovies = state.nowPlayingMovies.collectAsLazyPagingItems()
    val popularMovies = state.popularMovies.collectAsLazyPagingItems()
    val trendingTvSeries = state.trendingTvSeries.collectAsLazyPagingItems()
    val onAirTvSeries = state.onAirTvSeries.collectAsLazyPagingItems()
    val topRatedTvSeries = state.topRatedTvSeries.collectAsLazyPagingItems()
    val airingTodayTvSeries = state.airingTodayTvSeries.collectAsLazyPagingItems()
    val popularTvSeries = state.popularTvSeries.collectAsLazyPagingItems()

    val refreshing = false
    val pullRefreshState = rememberPullRefreshState(refreshing, { onEvent(HomeEvents.OnPullToRefresh) })

    Box(Modifier.pullRefresh(pullRefreshState)) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                FilmCategory(
                    items = listOf(
                        stringResource(R.string.movies), stringResource(R.string.tv_shows)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    state = state,
                    onEvent = onEvent,
                )
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.trending),
                        style = MaterialTheme.typography.titleMedium,
                    )

                    if (state.selectedFilmOption == stringResource(R.string.tv_shows)) {
                        PagedRow(items = trendingTvSeries, modifier = Modifier.fillMaxWidth(), content = {
                            FilmItem(
                                modifier = Modifier
                                    .height(220.dp)
                                    .width(250.dp)
                                    .clickable {
                                        onEvent(
                                            HomeEvents.NavigateToDetails(
                                                film = it.toFilm(
                                                    category = context.getString(R.string.trending)
                                                )
                                            )
                                        )
                                    },
                                imageUrl = it.posterPath.getImageUrl(),
                                animatedVisibilityScope = animatedVisibilityScope,
                                sharedTransitionKey = "${it.id}_${context.getString(R.string.trending)}",
                            )
                        })
                    } else {
                        PagedRow(items = trendingMovies, modifier = Modifier.fillMaxWidth(), content = {
                            FilmItem(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(230.dp)
                                    .clickable {
                                        onEvent(
                                            HomeEvents.NavigateToDetails(
                                                film = it.toFilm(
                                                    category = context.getString(R.string.trending)
                                                )
                                            )
                                        )
                                    },
                                imageUrl = it.posterPath.getImageUrl(),
                                animatedVisibilityScope = animatedVisibilityScope,
                                sharedTransitionKey = "${it.id}_${context.getString(R.string.trending)}",
                            )
                        })
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.top_rated),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    if (state.selectedFilmOption == stringResource(R.string.tv_shows)) {
                        PagedRow(items = topRatedTvSeries, modifier = Modifier.fillMaxWidth(), content = {
                            FilmItem(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(130.dp)
                                    .clickable {
                                        onEvent(
                                            HomeEvents.NavigateToDetails(
                                                film = it.toFilm(
                                                    category = context.getString(R.string.top_rated)
                                                )
                                            )
                                        )
                                    },
                                imageUrl = it.posterPath.getImageUrl(),
                                animatedVisibilityScope = animatedVisibilityScope,
                                sharedTransitionKey = "${it.id}_${context.getString(R.string.top_rated)}",
                            )
                        })
                    } else {
                        PagedRow(items = topRatedMovies, modifier = Modifier.fillMaxWidth(), content = {
                            FilmItem(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(130.dp)
                                    .clickable {
                                        onEvent(
                                            HomeEvents.NavigateToDetails(
                                                film = it.toFilm(
                                                    category = context.getString(R.string.top_rated)
                                                )
                                            )
                                        )
                                    },
                                imageUrl = it.posterPath.getImageUrl(),
                                animatedVisibilityScope = animatedVisibilityScope,
                                sharedTransitionKey = "${it.id}_${context.getString(R.string.top_rated)}",
                            )
                        })
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = if (state.selectedFilmOption == stringResource(R.string.tv_shows)) {
                            stringResource(R.string.airing_today)
                        } else {
                            stringResource(R.string.now_playing)
                        },
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                if (state.selectedFilmOption == stringResource(R.string.tv_shows)) {
                    PagedRow(items = airingTodayTvSeries, modifier = Modifier.fillMaxWidth(), content = {
                        FilmItem(
                            modifier = Modifier
                                .height(200.dp)
                                .width(130.dp)
                                .clickable {
                                    onEvent(
                                        HomeEvents.NavigateToDetails(
                                            film = it.toFilm(
                                                category = context.getString(R.string.airing_today)
                                            )
                                        )
                                    )
                                },
                            imageUrl = it.posterPath.getImageUrl(),
                            animatedVisibilityScope = animatedVisibilityScope,
                            sharedTransitionKey = "${it.id}_${context.getString(R.string.airing_today)}",
                        )
                    })
                } else {
                    PagedRow(items = nowPlayingMovies, modifier = Modifier.fillMaxWidth(), content = {
                        FilmItem(
                            modifier = Modifier
                                .height(200.dp)
                                .width(130.dp)
                                .clickable {
                                    onEvent(
                                        HomeEvents.NavigateToDetails(
                                            film = it.toFilm(
                                                category = context.getString(R.string.now_playing)
                                            )
                                        )
                                    )
                                },
                            imageUrl = it.posterPath.getImageUrl(),
                            animatedVisibilityScope = animatedVisibilityScope,
                            sharedTransitionKey = "${it.id}_${context.getString(R.string.now_playing)}",
                        )
                    })
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = stringResource(R.string.popular),
                        style = MaterialTheme.typography.titleMedium,
                    )
                    if (state.selectedFilmOption == stringResource(R.string.tv_shows)) {
                        PagedRow(items = popularTvSeries, modifier = Modifier.fillMaxWidth(), content = {
                            FilmItem(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(130.dp)
                                    .clickable {
                                        onEvent(
                                            HomeEvents.NavigateToDetails(
                                                film = it.toFilm(
                                                    category = context.getString(R.string.popular)
                                                )
                                            )
                                        )
                                    },
                                imageUrl = it.posterPath.getImageUrl(),
                                animatedVisibilityScope = animatedVisibilityScope,
                                sharedTransitionKey = "${it.id}_${context.getString(R.string.popular)}",
                            )
                        })
                    } else {
                        PagedRow(items = popularMovies, modifier = Modifier.fillMaxWidth(), content = {
                            FilmItem(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(130.dp)
                                    .clickable {
                                        onEvent(
                                            HomeEvents.NavigateToDetails(
                                                film = it.toFilm(
                                                    category = context.getString(R.string.popular)
                                                )
                                            )
                                        )
                                    },
                                imageUrl = it.posterPath.getImageUrl(),
                                animatedVisibilityScope = animatedVisibilityScope,
                                sharedTransitionKey = "${it.id}_${context.getString(R.string.popular)}",
                            )
                        })
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = if (state.selectedFilmOption == stringResource(R.string.tv_shows)) {
                            stringResource(R.string.on_air)
                        } else {
                            stringResource(R.string.upcoming)
                        },
                        style = MaterialTheme.typography.titleMedium,
                    )
                    if (state.selectedFilmOption == stringResource(R.string.tv_shows)) {
                        PagedRow(items = onAirTvSeries, modifier = Modifier.fillMaxWidth(), content = {
                            FilmItem(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(130.dp)
                                    .clickable {
                                        onEvent(
                                            HomeEvents.NavigateToDetails(
                                                film = it.toFilm(
                                                    category = context.getString(R.string.on_air)
                                                )
                                            )
                                        )
                                    },
                                imageUrl = it.posterPath.getImageUrl(),
                                animatedVisibilityScope = animatedVisibilityScope,
                                sharedTransitionKey = "${it.id}_${context.getString(R.string.on_air)}",
                            )
                        })
                    } else {
                        PagedRow(items = upcomingMovies, modifier = Modifier.fillMaxWidth(), content = {
                            FilmItem(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(130.dp)
                                    .clickable {
                                        onEvent(
                                            HomeEvents.NavigateToDetails(
                                                film = it.toFilm(
                                                    category = context.getString(R.string.upcoming)
                                                )
                                            )
                                        )
                                    },
                                imageUrl = it.posterPath.getImageUrl(),
                                animatedVisibilityScope = animatedVisibilityScope,
                                sharedTransitionKey = "${it.id}_${context.getString(R.string.upcoming)}",
                            )
                        })
                    }
                }
            }

        }

        PullRefreshIndicator(refreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))


    }
}


@Composable
fun <T : Any> PagedRow(
    modifier: Modifier = Modifier,
    items: LazyPagingItems<T>,
    content: @Composable (T) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp), contentAlignment = Alignment.Center
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items.itemCount) {
                val item = items[it]
                if (item != null) {
                    content(item)
                }
            }

            items.loadState.let { loadState ->
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .align(Alignment.Center),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                )
                            }
                        }
                    }

                    loadState.refresh is LoadState.NotLoading && items.itemCount < 1 -> {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .align(Alignment.Center),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "No data available",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }


                    loadState.refresh is LoadState.Error -> {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .align(Alignment.Center),
                                horizontalArrangement = Arrangement.Center,
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = when ((loadState.refresh as LoadState.Error).error) {
                                        is HttpException -> {
                                            "Oops, something went wrong!"
                                        }

                                        is IOException -> {
                                            "Couldn't reach server, check your internet connection!"
                                        }

                                        else -> {
                                            "Unknown error occurred"
                                        }
                                    },
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }

                    loadState.append is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .align(Alignment.Center),
                                    strokeWidth = 2.dp,
                                )
                            }
                        }
                    }

                    loadState.append is LoadState.Error -> {
                        item {
                            Box(
                                modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "An error occurred",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.FilmItem(
    modifier: Modifier = Modifier,
    sharedTransitionKey: String,
    animatedVisibilityScope: AnimatedContentScope,
    imageUrl: String,
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(imageUrl).crossfade(true).build(), placeholder = painterResource(R.drawable.ic_placeholder), error = painterResource(id = R.drawable.ic_placeholder), contentDescription = null, contentScale = ContentScale.Crop, modifier = modifier
            .sharedElement(
                state = rememberSharedContentState(
                    key = sharedTransitionKey
                ),
                animatedVisibilityScope = animatedVisibilityScope,
            )
            .fillMaxSize()
            .clip(shape = MaterialTheme.shapes.medium)
    )
}


@Composable
fun FilmCategory(
    items: List<String>,
    modifier: Modifier = Modifier,
    state: HomeState,
    onEvent: (HomeEvents) -> Unit,
) {
    Row(
        modifier = modifier, horizontalArrangement = Arrangement.Center
    ) {
        items.forEach { item ->
            val lineLength = animateFloatAsState(
                targetValue = if (item == state.selectedFilmOption) 2f else 0f,
                animationSpec = tween(
                    durationMillis = 300
                ),
                label = "lineLength",
            )

            val primaryColor = MaterialTheme.colorScheme.primary
            Text(text = item, modifier = Modifier
                .testTag(item)
                .padding(8.dp)
                .drawBehind {
                    if (item == state.selectedFilmOption) {
                        if (lineLength.value > 0f) {
                            drawLine(
                                color = primaryColor, start = Offset(
                                    size.width / 2f - lineLength.value * 10.dp.toPx(), size.height
                                ), end = Offset(
                                    size.width / 2f + lineLength.value * 10.dp.toPx(), size.height
                                ), strokeWidth = 2.dp.toPx(), cap = StrokeCap.Round
                            )
                        }
                    }
                }
                .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
                    onEvent(
                        HomeEvents.OnFilmOptionSelected(
                            item
                        )
                    )
                }, style = MaterialTheme.typography.headlineSmall.copy(
                color = if (item == state.selectedFilmOption) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(
                    .5f
                ),
            )
            )
        }
    }
}