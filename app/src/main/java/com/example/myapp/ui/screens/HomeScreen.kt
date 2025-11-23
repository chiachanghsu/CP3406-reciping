package com.example.myapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myapp.data.Repository
import com.example.myapp.data.model.Meal
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(repo: Repository, onOpenDetail: (String) -> Unit) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var meals by remember { mutableStateOf<List<Meal>>(emptyList()) }
    var loading by remember { mutableStateOf(false) }
    var isLoadingMore by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        scope.launch {
            loading = true
            repeat(5) {
                repo.random()?.let { meal ->
                    meals = meals + meal
                }
            }
            loading = false
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                val totalItems = meals.size
                if (lastVisibleIndex != null && lastVisibleIndex >= totalItems - 2 && !isLoadingMore && !loading) {
                    isLoadingMore = true
                    scope.launch {
                        repeat(3) {
                            repo.random()?.let { meal ->
                                meals = meals + meal
                            }
                        }
                        isLoadingMore = false
                    }
                }
            }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            Text(
                "Home",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
            
            if (loading && meals.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(meals, key = { it.id }) { meal ->
                        RandomMealCard(
                            meal = meal,
                            onOpenDetail = { onOpenDetail(meal.id) },
                            onToggleSave = {
                                scope.launch {
                                    val isSaved = repo.toggleSave(meal)
                                    snackbarHostState.showSnackbar(
                                        message = if (isSaved) "Recipe saved!" else "Recipe unsaved",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }
                        )
                    }
                    
                    if (isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RandomMealCard(
    meal: Meal,
    onOpenDetail: () -> Unit,
    onToggleSave: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = meal.thumb,
                contentDescription = meal.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                meal.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                (meal.area ?: "") + (if (!meal.category.isNullOrBlank()) " • ${meal.category}" else ""),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onOpenDetail,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("View Details")
                }
                OutlinedButton(
                    onClick = onToggleSave,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
            }
        }
    }
}
