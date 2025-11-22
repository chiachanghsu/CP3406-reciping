package com.example.myapp.ui.navigation


sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Search : Routes("search")
    data object Detail : Routes("detail/{mealId}") { const val ARG = "mealId" }
    data object Profile : Routes("profile")
}