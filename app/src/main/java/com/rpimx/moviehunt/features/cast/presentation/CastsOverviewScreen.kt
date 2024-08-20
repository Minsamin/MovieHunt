package com.rpimx.moviehunt.features.cast.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.rpimx.moviehunt.R
import com.rpimx.moviehunt.common.utils.Constants
import com.rpimx.moviehunt.features.cast.domain.model.Credits

@Composable
fun CastsOverviewScreen(
    credits: Credits, navController: NavController
) {
    CastsOverviewScreenContent(
        credits = credits, navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CastsOverviewScreenContent(
    credits: Credits, navController: NavController
) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = stringResource(R.string.casts),
                style = MaterialTheme.typography.titleLarge,
            )
        }, navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null,
                )
            }
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
        )
    }) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(), columns = GridCells.Fixed(2), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = credits.cast, key = { cast -> cast.id }) { cast ->
                CastItem(
                    imageSize = 170.dp, castImageUrl = "${Constants.IMAGE_BASE_UR}/${cast.profilePath}", castName = cast.name
                )
            }
        }
    }
}

@Composable
fun CastItem(
    imageSize: Dp,
    castName: String,
    castImageUrl: String,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(castImageUrl).crossfade(true).build(), placeholder = painterResource(R.drawable.ic_placeholder), error = painterResource(id = R.drawable.ic_placeholder), contentDescription = castName, contentScale = ContentScale.Crop, modifier = Modifier
                .size(imageSize)
                .clip(RoundedCornerShape(18.dp))
        )

        Text(
            text = castName, fontWeight = FontWeight.Medium, fontSize = 13.sp
        )
    }
}