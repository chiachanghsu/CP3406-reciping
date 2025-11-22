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
fun ProfileScreen(repo: Repository, onOpenDetail: (String) -> Unit) {
    val saved = remember { mutableStateListOf<Meal>() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            saved.clear()
            saved.addAll(repo.savedList())
        }
    }


    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Saved recipes", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        if (saved.isEmpty()) Text("Nothing saved yet") else LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(saved, key = { it.id }) { meal ->
                Card(Modifier.fillMaxWidth().clickable { onOpenDetail(meal.id) }) {
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
        }
    }
}