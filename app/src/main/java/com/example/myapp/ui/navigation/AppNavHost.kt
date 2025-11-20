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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.myapp.ui.screens.*

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    val back by nav.currentBackStackEntryAsState()
    val route = back?.destination?.route
    val showBars = route in listOf(Routes.Home, Routes.Search, Routes.Profile)

    Scaffold(
        topBar = {
            if (showBars) CenterAlignedTopAppBar(title = {
                Text(
                    when (route) {
                        Routes.Home -> "Reciping"
                        Routes.Search -> "Search"
                        Routes.Profile -> "Profile"
                        else -> ""
                    }
                )
            })
        },
        bottomBar = {
            if (showBars) NavigationBar {
                listOf(
                    Routes.Home to Icons.Outlined.Home,
                    Routes.Search to Icons.Outlined.Search,
                    Routes.Profile to Icons.Outlined.Person
                ).forEach { (r, icon) ->
                    val selected = route == r
                    NavigationBarItem(
                        selected = selected,
                        onClick = { nav.navigate(r) { popUpTo(nav.graph.startDestinationId) { inclusive = false }; launchSingleTop = true } },
                        icon = { Icon(icon, contentDescription = r) },
                        label = { Text(r.replaceFirstChar { it.uppercase() }) }
                    )
                }
            }
        }
    ) { inner ->
        Box(Modifier.padding(inner).fillMaxSize()) {
            NavHost(nav, startDestination = Routes.Home) {
                composable(Routes.Home) {
                    HomeScreen(onMealClick = { id -> nav.navigate("detail/$id") })
                }
                composable(Routes.Search) {
                    SearchScreen(onMealClick = { id -> nav.navigate("detail/$id") })
                }
                composable(Routes.Profile) {
                    ProfileScreen(onMealClick = { id -> nav.navigate("detail/$id") })
                }
                composable(
                    Routes.Detail,
                    arguments = listOf(navArgument("id") { type = NavType.StringType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")!!
                    RecipeDetailScreen(mealId = id)
                }
            }
        }
    }
}
