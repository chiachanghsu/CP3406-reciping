@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.myapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.myapp.ui.theme.MyAndroidAppTheme

@Composable
fun AppNavHost(startDestination: String = Routes.Splash) {
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
                                Routes.Home -> "Reciping"
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
                            popUpTo(nav.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { inner ->
        Box(Modifier.padding(inner).fillMaxSize()) {
            NavHost(navController = nav, startDestination = startDestination) {
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
    data class Item(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

    val items = listOf(
        Item(Routes.Home, "Home", Icons.Outlined.Home),
        Item(Routes.Search, "Search", Icons.Outlined.Search),
        Item(Routes.Profile, "Profile", Icons.Outlined.Person)
    )

    NavigationBar {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "App (Home)")
@Composable
fun AppNavHostPreview() {
    MyAndroidAppTheme { AppNavHost(startDestination = Routes.Home) }
}
