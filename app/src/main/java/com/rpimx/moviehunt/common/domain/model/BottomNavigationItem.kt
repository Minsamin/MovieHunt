package com.rpimx.moviehunt.common.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val label: String,
    val route: Any,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val hasNews: Boolean = false,
    val badgeCount: Int = 0,
)