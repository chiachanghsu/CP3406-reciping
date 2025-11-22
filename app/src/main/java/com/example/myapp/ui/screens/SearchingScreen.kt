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


@Composable
fun SearchingScreen(repo: Repository, onOpenDetail: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    var results by remember { mutableStateOf(listOf<Meal>()) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()


    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search by name") },
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            scope.launch {
                loading = true
                results = repo.search(query)
                loading = false
            }
        }) { Text("Search") }


        Spacer(Modifier.height(12.dp))
        if (loading) CircularProgressIndicator() else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(results) { meal ->
                MealRow(meal = meal, onClick = { onOpenDetail(meal.id) })
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