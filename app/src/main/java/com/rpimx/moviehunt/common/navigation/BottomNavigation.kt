package com.rpimx.moviehunt.common.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.rpimx.moviehunt.common.domain.model.BottomNavigationItem

@Composable
fun BottomBar(navController: NavController) {

    val bottomNavigationItems = listOf(
        BottomNavigationItem(
            label = DashboardScreenRoute.Home.routeName, route = DashboardScreenRoute.Home, selectedIcon = Icons.Filled.Home, unSelectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            label = DashboardScreenRoute.Search.routeName, route = DashboardScreenRoute.Search, selectedIcon = Icons.Filled.Search, unSelectedIcon = Icons.Outlined.Search
        ),
        BottomNavigationItem(
            label = DashboardScreenRoute.Bookmark.routeName, route = DashboardScreenRoute.Bookmark, selectedIcon = Icons.Filled.Favorite, unSelectedIcon = Icons.Outlined.Favorite
        ),
        BottomNavigationItem(
            label = DashboardScreenRoute.Settings.routeName, route = DashboardScreenRoute.Settings, selectedIcon = Icons.Filled.Settings, unSelectedIcon = Icons.Outlined.Settings
        ),
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val bottomBarDestination = bottomNavigationItems.any {
        currentDestination?.route == it.route::class.java.canonicalName?.toString()
    }
    if (bottomBarDestination) {
        NavigationBar {
            bottomNavigationItems.forEachIndexed { _, item ->
                NavigationBarItem(
                    modifier = Modifier.testTag(item.label),
                    selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                    onClick = {
                                navController.navigate(item.route, navOptions {
                                    popUpTo(navController.graph.findStartDestination().id)
                                    launchSingleTop = true
                                })
                              },
                    label = { Text(item.label) },
                    icon = {
                    BadgedBox(
                        badge = {
                        if (item.badgeCount > 0) {
                            Badge {
                                Text(item.badgeCount.toString())
                            }
                        } else if (item.hasNews) {
                            Badge()
                        }
                    }) {
                        Icon(imageVector = if (currentDestination?.hierarchy?.any {
                                it.route == item.route
                            } == true) item.selectedIcon else item.unSelectedIcon, contentDescription = item.label)
                    }
                })
            }
        }
    }
}