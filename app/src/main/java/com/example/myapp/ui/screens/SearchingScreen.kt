package com.example.myapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapp.data.Repository
import com.example.myapp.data.model.Meal
import kotlinx.coroutines.launch

enum class SortOption {
    NAME, AREA, CATEGORY
}

@Composable
fun SearchingScreen(repo: Repository, onOpenDetail: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf(listOf<Meal>()) }
    var loading by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf(SortOption.NAME) }
    val scope = rememberCoroutineScope()

    // Sort results based on selected option
    val sortedResults = remember(results, sortOption) {
        when (sortOption) {
            SortOption.NAME -> results.sortedBy { it.name.lowercase() }
            SortOption.AREA -> results.sortedBy { it.area ?: "" }
            SortOption.CATEGORY -> results.sortedBy { it.category ?: "" }
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search by name") },
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                scope.launch {
                    loading = true
                    results = repo.search(query)
                    loading = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        if (results.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                "Sort by:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = sortOption == SortOption.NAME,
                    onClick = { sortOption = SortOption.NAME },
                    label = { Text("Name") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = sortOption == SortOption.AREA,
                    onClick = { sortOption = SortOption.AREA },
                    label = { Text("Area") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = sortOption == SortOption.CATEGORY,
                    onClick = { sortOption = SortOption.CATEGORY },
                    label = { Text("Category") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (sortedResults.isEmpty() && query.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No results found")
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sortedResults, key = { it.id }) { meal ->
                    MealRow(meal = meal, onClick = { onOpenDetail(meal.id) })
                }
            }
        }
    }
}


@Composable
private fun MealRow(meal: Meal, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(Modifier.padding(12.dp)) {
            AsyncImage(model = meal.thumb, contentDescription = meal.name, modifier = Modifier.size(64.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.fillMaxWidth()) {
                Text(meal.name, style = MaterialTheme.typography.titleMedium)
                Text((meal.area ?: "") + (if (!meal.category.isNullOrBlank()) " • ${meal.category}" else ""))
            }
        }
    }
}