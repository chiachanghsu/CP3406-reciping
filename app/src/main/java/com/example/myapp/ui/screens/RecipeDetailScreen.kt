package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun RecipeDetailScreen(repo: Repository, mealId: String) {
    var meal by remember { mutableStateOf<Meal?>(null) }
    var loading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(mealId) {
        scope.launch {
            loading = true
            meal = repo.lookup(mealId)
            loading = false
        }
    }

    if (loading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else meal?.let { m ->
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            AsyncImage(
                model = m.thumb, 
                contentDescription = m.name, 
                modifier = Modifier.fillMaxWidth().height(220.dp)
            )
            Spacer(Modifier.height(12.dp))
            Text(m.name, style = MaterialTheme.typography.headlineSmall)
            Text((m.area ?: "") + (if (!m.category.isNullOrBlank()) " • ${m.category}" else ""))
            Spacer(Modifier.height(12.dp))
            Text("Ingredients", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            if (m.ingredients.isEmpty()) {
                Text("No ingredients listed")
            } else {
                m.ingredients.forEach { (ing, mea) ->
                    Text("• $ing${if (mea.isNotBlank()) ": $mea" else ""}")
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("Instructions", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(m.instructions ?: "No instructions available")
            Spacer(Modifier.height(20.dp))
            Button(onClick = { 
                scope.launch { 
                    repo.toggleSave(m) 
                }
            }) { 
                Text("Save / Unsave") 
            }
        }
    } ?: run { 
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Recipe not found")
        }
    }
}