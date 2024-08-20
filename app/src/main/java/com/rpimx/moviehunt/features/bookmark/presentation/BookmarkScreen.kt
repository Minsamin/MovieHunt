package com.rpimx.moviehunt.features.bookmark.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.rpimx.moviehunt.R
import com.rpimx.moviehunt.common.domain.model.toFilm
import com.rpimx.moviehunt.common.navigation.BottomBar
import com.rpimx.moviehunt.common.navigation.DashboardScreenRoute
import com.rpimx.moviehunt.common.utils.Constants.TYPE_MOVIE
import com.rpimx.moviehunt.common.utils.Constants.TYPE_TV_SERIES
import com.rpimx.moviehunt.features.bookmark.data.local.Bookmark
import com.rpimx.moviehunt.features.details.presentation.common.VoteAverageRatingIndicator
import kotlinx.coroutines.delay

@Composable
fun BookmarkScreensScreen(
    navController: NavController, viewModel: BookmarksViewModel = hiltViewModel()
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    val bookmarkFilms by viewModel.bookmarks.collectAsState()

    BookmarkScreensScreenContent(navController = navController, bookmarkItems = bookmarkFilms, showDeleteAlertDialog = openDialog, onClickDeleteAllBookmarkScreens = {
        openDialog = true
    }, onDismissDeleteConsentDialog = {
        openDialog = false
    }, onDeleteOneBookmarkScreen = { bookmark ->
        viewModel.deleteFromBookmark(bookmark)
    }, onClickABookmarkScreen = { bookmark ->
        if (bookmark.type == TYPE_TV_SERIES) {
            navController.navigate(
                DashboardScreenRoute.DetailsScreenArguments(
                    film = bookmark.toFilm()
                )
            )
        } else if (bookmark.type == TYPE_MOVIE) {
            navController.navigate(
                DashboardScreenRoute.DetailsScreenArguments(
                    film = bookmark.toFilm()
                )
            )
        }
    }, onConfirmDeleteAllBookmarkScreens = {
        viewModel.deleteAllBookmarks()
        openDialog = false
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookmarkScreensScreenContent(
    navController: NavController,
    bookmarkItems: List<Bookmark>,
    showDeleteAlertDialog: Boolean,
    onDismissDeleteConsentDialog: () -> Unit,
    onConfirmDeleteAllBookmarkScreens: () -> Unit,
    onClickDeleteAllBookmarkScreens: () -> Unit,
    onDeleteOneBookmarkScreen: (Bookmark) -> Unit,
    onClickABookmarkScreen: (Bookmark) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize(), bottomBar = {
        BottomBar(navController)
    }, topBar = {
        TopAppBar(modifier = Modifier.fillMaxWidth(), title = {
            Text(
                text = stringResource(R.string.bookmarks),
                style = MaterialTheme.typography.titleLarge,
            )
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ), actions = {
            IconButton(
                onClick = onClickDeleteAllBookmarkScreens
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                )
            }
        })
    }) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2), modifier = Modifier
                .padding(it)
                .testTag("BookmarkGrid"), contentPadding = PaddingValues(horizontal = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = bookmarkItems, key = { bookmarkFilm: Bookmark ->
                bookmarkFilm.id
            }) { bookmark ->
                SwipeToDeleteContainer(item = bookmark, onDelete = onDeleteOneBookmarkScreen, content = {
                    FilmItem(modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp), filmItem = bookmark, onClick = {
                        onClickABookmarkScreen(bookmark)
                    })
                })
            }
        }

        if (bookmarkItems.isEmpty()) {
            Column(
                Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(250.dp), painter = painterResource(id = R.drawable.ic_empty_bookmark), contentDescription = null
                )
            }
        }

        if (showDeleteAlertDialog) {
            AlertDialog(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), tonalElevation = 0.dp, onDismissRequest = {
                onDismissDeleteConsentDialog()
            }, title = {
                Text(text = stringResource(R.string.delete_all_bookmarks))
            }, text = {
                Text(text = stringResource(R.string.are_you_want_to_delete_all))
            }, confirmButton = {
                Button(
                    onClick = onConfirmDeleteAllBookmarkScreens,
                ) {
                    Text(text = stringResource(R.string.yes))
                }
            }, dismissButton = {
                Button(
                    onClick = onDismissDeleteConsentDialog,
                ) {
                    Text(text = stringResource(R.string.no))
                }
            }, shape = RoundedCornerShape(10.dp)
            )
        }
    }
}

@Composable
fun FilmItem(
    filmItem: Bookmark,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.clip(RoundedCornerShape(8.dp)), onClick = onClick,
        shape = RoundedCornerShape(8.dp),
    ) {
        Box {
            Image(painter = rememberImagePainter(data = filmItem.image, builder = {
                placeholder(R.drawable.ic_placeholder)
                crossfade(true)
            }), modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop, contentDescription = "Movie Banner")
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                Pair(0.3f, Transparent), Pair(
                                    1.5f, MaterialTheme.colorScheme.background
                                )
                            )
                        )
                    )
            )

            FilmDetails(
                title = filmItem.title, releaseDate = filmItem.releaseDate, rating = filmItem.rating
            )
        }
    }
}

@Composable
fun FilmDetails(
    modifier: Modifier = Modifier, title: String, releaseDate: String, rating: Float
) {
    Row(
        modifier = modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceEvenly

        ) {
            Column(
                modifier = Modifier.weight(0.7f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = releaseDate,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Light,
                    ),
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .wrapContentSize()
            ) {
                VoteAverageRatingIndicator(
                    percentage = rating
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T, onDelete: (T) -> Unit, animationDuration: Int = 500, content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberSwipeToDismissBoxState(confirmValueChange = { value ->
        if (value == SwipeToDismissBoxValue.EndToStart) {
            isRemoved = true
            true
        } else {
            false
        }
    })

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved, exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration), shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(state = state, backgroundContent = {
            DeleteBackground(swipeDismissState = state)
        }, content = { content(item) }, enableDismissFromStartToEnd = false
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState
) {
    val color by animateColorAsState(
        when (swipeDismissState.targetValue) {
            SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.background
            else -> MaterialTheme.colorScheme.primary
        }, label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp), contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}