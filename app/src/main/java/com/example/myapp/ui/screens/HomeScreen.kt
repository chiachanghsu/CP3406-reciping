@file:OptIn(androidx.compose.material.ExperimentalMaterialApi::class)

package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapp.data.remote.MealsApi
import com.example.myapp.data.remote.Meal
import com.example.myapp.ui.theme.MyAndroidAppTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Data & UI state
    val meals = remember { mutableStateListOf<Meal>() }
    var refreshing by remember { mutableStateOf(false) }
    var loadingMore by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Fetch a batch of random meals
    fun loadBatch(count: Int = 6, replace: Boolean = false) {
        if (loadingMore) return
        loadingMore = true
        error = null
        scope.launch {
            try {
                val newOnes = buildList {
                    repeat(count) {
                        val r = MealsApi.service.randomMeal()
                        r.meals?.firstOrNull()?.let { add(it) }
                    }
                }
                if (replace) {
                    meals.clear()
                }
                meals.addAll(newOnes)
            } catch (t: Throwable) {
                error = t.message ?: "Failed to load recipes"
            } finally {
                loadingMore = false
                refreshing = false
            }
        }
    }

    // Initial load
    LaunchedEffect(Unit) { loadBatch() }

    // Pull to refresh
    val pullState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = {
            refreshing = true
            loadBatch(count = 6, replace = true)
        }
    )

    // Infinite scroll trigger (near the end)
    val shouldLoadMore by remember {
        derivedStateOf {
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = listState.layoutInfo.totalItemsCount
            total > 0 && last >= total - 3 && !refreshing && !loadingMore
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) loadBatch(count = 4)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullState)
    ) {
        // Content
        when {
            error != null && meals.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = error ?: "Error", style = MaterialTheme.typography.bodyLarge)
                }
            }
            meals.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Loading recipes…", style = MaterialTheme.typography.bodyLarge)
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(meals) { m ->
                        MealCard(m)
                    }
                    // Optional tiny footer spacing
                    item { Spacer(Modifier.height(24.dp)) }
                }
            }
        }

        // Pull-to-refresh indicator
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
private fun MealCard(m: Meal) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.fillMaxWidth()) {
            AsyncImage(
                model = m.strMealThumb,
                contentDescription = m.strMeal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Column(Modifier.padding(12.dp)) {
                Text(
                    text = m.strMeal,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val meta = listOfNotNull(m.strArea, m.strCategory).joinToString(" • ")
                if (meta.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = meta,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePreview() {
    // Preview with fake data
    val fake = Meal(
        id = "1234",
        strMeal = "Preview Pasta",
        strMealThumb = "https://www.themealdb.com/images/media/meals/ustsqw1468250014.jpg",
        strArea = "Italian",
        strCategory = "Pasta"
    )
    MyAndroidAppTheme {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            MealCard(fake)
            Spacer(Modifier.height(12.dp))
            MealCard(fake.copy(strMeal = "Another Dish"))
        }
    }
}
