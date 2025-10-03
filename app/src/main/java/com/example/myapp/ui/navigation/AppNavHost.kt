@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.myapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapp.ui.screens.HomeScreen
import com.example.myapp.ui.screens.ProfileScreen
import com.example.myapp.ui.screens.SearchScreen
import com.example.myapp.ui.screens.SplashScreen


@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route
    val showBars = route in listOf(Routes.Home, Routes.Search, Routes.Profile)

    Scaffold(
        topBar = {
            if (showBars) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            when (route) {
                                Routes.Home -> "reciping"
                                Routes.Search -> "Search"
                                Routes.Profile -> "Profile"
                                else -> ""
                            }
                        )
                    }
                )
            }
        },
        bottomBar = {
            if (showBars) {
                BottomNav(
                    currentDestination = backStack?.destination,
                    onNavigate = { r ->
                        nav.navigate(r) {
                            popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { inner ->
        Box(Modifier.padding(inner).fillMaxSize()) {
            NavHost(navController = nav, startDestination = Routes.Splash) {
                composable(Routes.Splash) {
                    SplashScreen {
                        nav.navigate(Routes.Home) {
                            popUpTo(Routes.Splash) { inclusive = true }
                        }
                    }
                }
                composable(Routes.Home) { HomeScreen() }
                composable(Routes.Search) { SearchScreen() }
                composable(Routes.Profile) { ProfileScreen() }
            }
        }
    }
}

@Composable
private fun BottomNav(
    currentDestination: NavDestination?,
    onNavigate: (String) -> Unit
) {
    val items = listOf(
        Triple(Routes.Home, "Home", Icons.Outlined.Home),
        Triple(Routes.Search, "Search", Icons.Outlined.Search),
        Triple(Routes.Profile, "Profile", Icons.Outlined.Person)
    )
    NavigationBar {
        items.forEach { (route, label, icon) ->
            val selected = currentDestination?.hierarchy?.any { it.route == route } == true
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(route) },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}
