package com.rpimx.moviehunt.features.details.presentation.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rpimx.moviehunt.R
import com.rpimx.moviehunt.common.domain.model.Film
import com.rpimx.moviehunt.common.utils.getImageUrl
import com.rpimx.moviehunt.features.bookmark.data.local.Bookmark
import com.rpimx.moviehunt.features.details.presentation.DetailsEvents

@Composable
fun DetailsScreenActions(
    modifier: Modifier = Modifier,
    onEvents: (DetailsEvents) -> Unit,
    isLiked: Boolean,
    film: Film,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            modifier = Modifier.clickable {
                onEvents(DetailsEvents.NavigateBack)
            }, imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null
        )

        CircleButton(
            modifier = Modifier.testTag("AddToBookMark"),
            onClick = {
                if (isLiked) {
                    onEvents(
                        DetailsEvents.RemoveFromBookmark(
                            Bookmark(
                                bookmark = false,
                                id = film.id,
                                type = film.type,
                                image = film.image.getImageUrl(),
                                title = film.name,
                                releaseDate = film.releaseDate,
                                rating = film.rating,
                                overview = film.overview,
                            )
                        )
                    )
                } else {
                    onEvents(
                        DetailsEvents.AddToBookmark(
                            Bookmark(
                                bookmark = true,
                                id = film.id,
                                type = film.type,
                                image = film.image.getImageUrl(),
                                title = film.name,
                                releaseDate = film.releaseDate,
                                rating = film.rating,
                                overview = film.overview,
                            )
                        )
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite, tint = if (isLiked) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.background
                }, contentDescription = if (isLiked) {
                    stringResource(id = R.string.unlike)
                } else {
                    stringResource(id = R.string.like)
                }
            )
        }
    }
}

@Composable
fun CircleButton(
    modifier: Modifier = Modifier, onClick: () -> Unit, containerColor: Color = MaterialTheme.colorScheme.primary, borderStroke: BorderStroke? = null, content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .border(borderStroke ?: BorderStroke(0.dp, containerColor), CircleShape)
            .background(
                color = containerColor, shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
