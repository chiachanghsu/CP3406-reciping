package com.example.myapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapp.data.remote.Meal
import com.example.myapp.ui.viewmodel.HomeRandomViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun HomeScreen(
    onMealClick: (String) -> Unit,
    vm: HomeRandomViewModel = viewModel()
) {
    // observe state from VM
    val meals: List<Meal> by vm.meals.collectAsState(initial = emptyList())

    // simple “loading” while we trigger refresh; if you have a loading StateFlow, use that instead
    val swipeState = rememberSwipeRefreshState(isRefreshing = false)

    SwipeRefresh(
        state = swipeState,
        onRefresh = { vm.refresh() },  // fetch another batch of random meals
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(meals, key = { it.id }) { m ->
                MealRow(meal = m, onClick = { onMealClick(m.id) })
                Divider()
            }
        }
    }
}

@Composable
private fun MealRow(meal: Meal, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp),
        leadingContent = {
            AsyncImage(
                model = meal.strMealThumb,
                contentDescription = meal.strMeal,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .fillMaxSize(fraction = 0.0f) // ListItem will size the image slot
            )
        },
        headlineContent = { Text(meal.strMeal) },
        supportingContent = {
            val meta = listOfNotNull(meal.strCategory, meal.strArea)
                .filter { it.isNotBlank() }
                .joinToString(" · ")
            if (meta.isNotEmpty()) {
                Text(
                    text = meta,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}
