package com.rpimx.moviehunt.features.settings.presentation

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rpimx.moviehunt.R
import com.rpimx.moviehunt.common.navigation.BottomBar
import com.rpimx.moviehunt.features.settings.domain.model.SettingsItem
import com.rpimx.moviehunt.features.settings.domain.model.themes
import com.rpimx.moviehunt.ui.theme.MovieHuntTheme

@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = hiltViewModel()) {
    var shouldShowThemesDialog by rememberSaveable { mutableStateOf(false) }

    SettingsScreenContent(navController = navController, onEvent = { event ->
        when (event) {
            SettingsScreenEvents.ShowThemesDialog -> {
                shouldShowThemesDialog = true
            }
        }
    })



    if (shouldShowThemesDialog) {
        ThemesDialog(selectedTheme = viewModel.theme.collectAsState().value, onDismiss = {
            shouldShowThemesDialog = false
        }, onSelectTheme = {
            shouldShowThemesDialog = false
            viewModel.updateTheme(it)
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    navController: NavController, onEvent: (SettingsScreenEvents) -> Unit
) {
    Scaffold(bottomBar = {
        BottomBar(navController)
    }, topBar = {
        TopAppBar(
            title = {
                Image(
                    painterResource(
                        id = R.drawable.ic_placeholder
                    ), contentDescription = "App logo", modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .padding(8.dp)
                )
            },
        )
    }) { innerPadding ->
        val context = LocalContext.current

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(context.accountItems()) { index, item ->
                AccountItems(accountItem = item, onClick = {
                    when (item.title) {
                        context.getString(R.string.change_theme) -> {
                            onEvent(
                                SettingsScreenEvents.ShowThemesDialog
                            )
                        }
                    }
                })

                if (index != context.accountItems().size - 1) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onBackground.copy(.5f),
                        thickness = .5.dp,
                    )
                }
            }
        }
    }
}


@Composable
fun AccountItems(
    accountItem: SettingsItem,
    onClick: () -> Unit = {},
) {
    Column(modifier = Modifier.clickable {
            onClick()
        }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = accountItem.icon),
                contentDescription = accountItem.title,
                tint = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = accountItem.title,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun ThemesDialog(
    onDismiss: () -> Unit,
    onSelectTheme: (Int) -> Unit,
    selectedTheme: Int,
) {
    AlertDialog(containerColor = MaterialTheme.colorScheme.background, onDismissRequest = { onDismiss() }, title = {
        Text(
            text = "Themes", style = MaterialTheme.typography.titleLarge
        )
    }, text = {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(themes) { theme ->
                ThemeItem(
                    themeName = theme.themeName, themeValue = theme.themeValue, icon = theme.icon, onSelectTheme = onSelectTheme, isSelected = theme.themeValue == selectedTheme
                )
            }
        }
    }, confirmButton = {
        Text(text = "OK", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary, modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable { onDismiss() })
    })
}

@Composable
fun ThemeItem(
    themeName: String,
    themeValue: Int,
    icon: Int,
    onSelectTheme: (Int) -> Unit,
    isSelected: Boolean,
) {
    Card(shape = MaterialTheme.shapes.large, colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.background
    ), onClick = {
        onSelectTheme(themeValue)
    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp, horizontal = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(.75f), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = icon), contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(12.dp), text = themeName, style = MaterialTheme.typography.labelMedium
                )
            }

            if (isSelected) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                )
            }
        }
    }
}

private fun Context.accountItems() = listOf(
    SettingsItem(
        getString(R.string.change_theme), R.drawable.ic_theme
    ),
)


@Preview
@Composable
fun SettingsScreenPreview() {
    MovieHuntTheme {
        SettingsScreenContent(navController = rememberNavController(), onEvent = {})
    }
}