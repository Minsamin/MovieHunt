package com.rpimx.moviehunt.features.details.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rpimx.moviehunt.R
import com.rpimx.moviehunt.common.utils.Constants
import com.rpimx.moviehunt.features.cast.presentation.CastItem
import com.rpimx.moviehunt.features.details.presentation.DetailsEvents
import com.rpimx.moviehunt.features.details.presentation.DetailsState

@Composable
fun CastDetails(
    state: DetailsState,
    modifier: Modifier = Modifier,
    onEvent: (DetailsEvents) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        if (state.isLoadingCasts) {
            CircularProgressIndicator()
        }

        if (state.errorCasts != null) {
            Text(text = state.errorCasts)
        }

        Spacer(modifier = Modifier.size(8.dp))

        if (state.isLoadingCasts.not() && state.errorCasts == null && state.credits != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.cast),
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleMedium,
                )

                Row(
                    modifier = Modifier.clickable {
                        onEvent(DetailsEvents.NavigateToCastsScreen(state.credits))
                    }, horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.view_all),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward, tint = MaterialTheme.colorScheme.primary, contentDescription = null
                    )
                }
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), content = {
                items(state.credits.cast.take(6)) { cast ->
                    CastItem(
                        imageSize = 80.dp, castImageUrl = "${Constants.IMAGE_BASE_UR}/${cast.profilePath}", castName = cast.name
                    )
                }
            })
        }
    }
}
