package com.example.myapp.ui.navigation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapp.data.Repository
import com.example.myapp.ui.screens.*
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person



@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    val repo = remember { Repository }


    Scaffold(
        bottomBar = { BottomBar(nav) }
    ) { padding ->
        NavGraph(nav, repo, Modifier.padding(padding))
    }
}


@Composable
private fun BottomBar(nav: NavHostController) {
    val items = listOf(Routes.Home, Routes.Search, Routes.Profile)
    NavigationBar {
        val currentRoute = nav.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { r ->
            NavigationBarItem(
                selected = currentRoute?.startsWith(r.route.substringBefore("/{")) == true,
                onClick = {
                    nav.navigate(r.route.substringBefore("/{")) {
                        popUpTo(Routes.Home.route) { inclusive = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    when (r) {
                        Routes.Home -> Icon(Icons.Filled.Home, null)
                        Routes.Search -> Icon(Icons.Filled.Search, null)
                        Routes.Profile -> Icon(Icons.Filled.Person, null)
                        else -> {}
                    }
                },
                label = { Text(r.route.substringBefore("/{").replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}


@Composable
private fun NavGraph(nav: NavHostController, repo: Repository, modifier: Modifier = Modifier) {
    NavHost(navController = nav, startDestination = Routes.Home.route, modifier = modifier) {
        composable(Routes.Home.route) {
            HomeScreen(
                repo = repo,
                onOpenDetail = { nav.navigate("detail/$it") }
            )
        }
        composable(Routes.Search.route) {
            SearchingScreen(
                repo = repo,
                onOpenDetail = { nav.navigate("detail/$it") }
            )
        }
        composable(Routes.Profile.route) {
            ProfileScreen(repo = repo, onOpenDetail = { nav.navigate("detail/$it") })
        }
        composable(Routes.Detail.route) { backStack ->
            val id = backStack.arguments?.getString(Routes.Detail.ARG) ?: return@composable
            RecipeDetailScreen(repo = repo, mealId = id)
        }
    }
}