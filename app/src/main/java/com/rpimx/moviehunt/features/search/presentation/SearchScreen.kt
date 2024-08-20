package com.rpimx.moviehunt.features.search.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rpimx.moviehunt.R
import com.rpimx.moviehunt.common.navigation.BottomBar
import com.rpimx.moviehunt.common.navigation.DashboardScreenRoute
import com.rpimx.moviehunt.common.utils.Constants
import com.rpimx.moviehunt.features.search.domain.model.search.Search
import com.rpimx.moviehunt.features.search.domain.model.search.toFilm
import retrofit2.HttpException
import java.io.IOException

@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    navController: NavController, viewModel: SearchViewModel = hiltViewModel()
) {
    val searchUiState by viewModel.searchState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    SearchScreenContent(navController = navController, state = searchUiState, onEvent = { event ->
        when (event) {
            is SearchEvents.SearchFilm -> {
                viewModel.searchAll(event.searchTerm)
                keyboardController?.hide()
            }

            is SearchEvents.SearchTermChanged -> {
                viewModel.updateSearchTerm(event.value)
                viewModel.searchAll(event.value)
            }

            is SearchEvents.OpenFilmDetails -> {
                keyboardController?.hide()
                val search = event.search
                if (search != null) {
                    navController.navigate(
                        DashboardScreenRoute.DetailsScreenArguments(
                            film = search.toFilm()
                        )
                    )
                }
            }

            SearchEvents.ClearSearchTerm -> {
                viewModel.clearSearch()
            }
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    navController: NavController,
    state: SearchState,
    onEvent: (SearchEvents) -> Unit,
) {
    val searchResult = state.searchResult.collectAsLazyPagingItems()

    Scaffold(bottomBar = {
        BottomBar(navController)
    }, topBar = {
        TopAppBar(
            modifier = Modifier.fillMaxWidth(), title = {
                Text(
                    text = stringResource(R.string.search_title),
                    style = MaterialTheme.typography.titleLarge,
                )
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background
            )
        )
    }) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            SearchBar(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .height(56.dp),
                onEvent = onEvent,
                state = state,
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("SearchList"),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(searchResult.itemCount) { index ->
                    val search = searchResult[index]
                    SearchItem(search = search, onClick = {
                        onEvent(SearchEvents.OpenFilmDetails(search))
                    })
                }

                searchResult.loadState.let { loadState ->
                    when {
                        loadState.refresh is LoadState.Loading && state.searchTerm.isNotEmpty() -> {
                            item {
                                Column(
                                    modifier = Modifier.fillParentMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    CircularProgressIndicator(
                                        strokeWidth = 2.dp,
                                    )
                                }
                            }
                        }

                        loadState.refresh is LoadState.NotLoading && searchResult.itemCount < 1 -> {
                            item {
                                Column(
                                    modifier = Modifier.fillParentMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        modifier = Modifier.size(250.dp), painter = painterResource(id = R.drawable.ic_empty_bookmark), contentDescription = null
                                    )
                                }
                            }
                        }


                        loadState.refresh is LoadState.Error -> {
                            item {
                                Column(
                                    modifier = Modifier.fillParentMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
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
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                    )
                                }
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
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
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    state: SearchState,
    onEvent: (SearchEvents) -> Unit,
) {
    TextField(
        modifier = modifier.testTag("SearchBar"),
        value = state.searchTerm,
        onValueChange = {
            onEvent(SearchEvents.SearchTermChanged(it))
        },
        placeholder = {
            Text(
                text = stringResource(R.string.search),
                color = MaterialTheme.colorScheme.onBackground.copy(.5f),
            )
        },
        shape = MaterialTheme.shapes.large,
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Words,
            keyboardType = KeyboardType.Text,
        ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedContainerColor = MaterialTheme.colorScheme.background,
        ),
        maxLines = 1,
        singleLine = true,
        trailingIcon = {
            if (state.searchTerm.isNotEmpty()) {
                IconButton(onClick = {
                    onEvent(SearchEvents.ClearSearchTerm)
                }) {
                    Icon(
                        imageVector = Icons.Default.Close, tint = MaterialTheme.colorScheme.onBackground.copy(.5f), contentDescription = null
                    )
                }
            }
        },
    )
}


@Composable
fun SearchItem(
    modifier: Modifier = Modifier, search: Search?, onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable {
                onClick()
            }, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data("${Constants.IMAGE_BASE_UR}/${search?.posterPath}").crossfade(true).build(),
                placeholder = painterResource(R.drawable.ic_placeholder),
                error = painterResource(id = R.drawable.ic_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(0.35f),
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(.7f),
                    text = (search?.name?.trim() ?: search?.originalName?.trim() ?: search?.originalTitle?.trim() ?: "---"),
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                )

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = search?.overview ?: "No description",
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall,
                )

                (search?.firstAirDate ?: search?.releaseDate)?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = it,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}
