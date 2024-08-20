package com.rpimx.moviehunt.features.details.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rpimx.moviehunt.R
import com.rpimx.moviehunt.common.domain.model.Film
import com.rpimx.moviehunt.common.navigation.DashboardScreenRoute
import com.rpimx.moviehunt.features.details.presentation.common.CastDetails
import com.rpimx.moviehunt.features.details.presentation.common.DetailsScreenActions
import com.rpimx.moviehunt.features.details.presentation.common.VoteAverageRatingIndicator

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailsScreen(
    film: Film,
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getFilmDetails(
            filmId = film.id, filmType = film.type
        )
    }
    val isFilmFavorite = viewModel.isBookmarked(film.id).collectAsState(initial = false).value
    val filmDetailsUiState by viewModel.detailsState.collectAsState()

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .testTag("DetailsScreen")
        ) {
            DetailsScreenContent(film = film, isLiked = isFilmFavorite, state = filmDetailsUiState, animatedVisibilityScope = animatedVisibilityScope, onEvents = { event ->
                when (event) {
                    is DetailsEvents.NavigateBack -> {
                        navController.popBackStack()
                    }

                    is DetailsEvents.NavigateToCastsScreen -> {
                        navController.navigate(
                            DashboardScreenRoute.CastsOverviewScreenArguments(event.credits)
                        )
                    }

                    is DetailsEvents.AddToBookmark -> {
                        viewModel.addToBookmark(
                            event.bookmark
                        )
                    }

                    is DetailsEvents.RemoveFromBookmark -> {
                        viewModel.deleteFromBookmark(
                            event.bookmark
                        )
                    }
                }
            })
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.DetailsScreenContent(
    film: Film,
    state: DetailsState,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onEvents: (DetailsEvents) -> Unit,
    isLiked: Boolean,
) {
    Scaffold(modifier = Modifier.sharedBounds(
        sharedContentState = rememberSharedContentState(key = "${film.id}_${film.category}"),
        animatedVisibilityScope = animatedVisibilityScope,
    ), topBar = {
        DetailsScreenActions(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            onEvents = onEvents,
            isLiked = isLiked,
            film = film,
        )
    }) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Box(
                    contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current).data(film.image).crossfade(true).build(), placeholder = painterResource(R.drawable.ic_placeholder), contentDescription = "Movie Banner", contentScale = ContentScale.Fit, modifier = Modifier
                            .height(450.dp)
                            .clip(RoundedCornerShape(18.dp))
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(), verticalAlignment = Alignment.Bottom
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(0.80f),
                            text = film.name,
                            style = MaterialTheme.typography.titleLarge,
                        )
                        VoteAverageRatingIndicator(
                            percentage = film.rating
                        )
                    }
                }
            }


            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp)) {
                                append(
                                    stringResource(R.string.release_date)
                                )
                            }
                            append(": ")
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp)) {
                                append(film.releaseDate)
                            }
                        },
                        style = MaterialTheme.typography.bodySmall,
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = film.overview,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            item {
                CastDetails(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    state = state,
                    onEvent = onEvents,
                )
            }

            if (state.isLoading) {
                item {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            if (state.isLoading.not() && state.error != null) {
                item {
                    Text(
                        text = state.error,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}