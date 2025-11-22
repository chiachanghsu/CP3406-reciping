package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    var meal by remember { mutableStateOf<Meal?>(null) }
    var loading by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        scope.launch {
            loading = true
            meal = repo.random()
            loading = false
        }
    }


    Column(Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Random recipe", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        if (loading) {
            CircularProgressIndicator()
        } else meal?.let { m ->
            AsyncImage(model = m.thumb, contentDescription = m.name, modifier = Modifier.height(200.dp).fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            Text(m.name, style = MaterialTheme.typography.titleLarge)
            Text((m.area ?: "") + (if (!m.category.isNullOrBlank()) " • ${m.category}" else ""))
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onOpenDetail(m.id) }) { Text("Open") }
                OutlinedButton(onClick = { 
                    scope.launch { 
                        repo.toggleSave(m) 
                    }
                }) { 
                    Text("Save/Unsave") 
                }
            }
        } ?: Text("No recipe yet")
    }
}