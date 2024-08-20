package com.rpimx.moviehunt.features.settings.domain.model

import com.rpimx.moviehunt.R
import com.rpimx.moviehunt.ui.theme.Theme

data class AppTheme(
    val themeName: String, val themeValue: Int, val icon: Int
)

val themes = listOf(
    AppTheme(
        themeName = "Light Mode", themeValue = Theme.LIGHT_THEME.themeValue, icon = R.drawable.light_mode
    ),
    AppTheme(
        themeName = "Dark Mode", themeValue = Theme.DARK_THEME.themeValue, icon = R.drawable.dark_mode
    ),
)