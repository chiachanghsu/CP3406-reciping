package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapp.data.remote.ingredients
import com.example.myapp.ui.viewmodel.DetailViewModel

@Composable
fun RecipeDetailScreen(
    mealId: String,
    vm: DetailViewModel = viewModel()
) {
    val meal by vm.meal.collectAsState()
    LaunchedEffect(mealId) { vm.load(mealId) }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Meal detail")
        Spacer(Modifier.height(8.dp))

        meal?.let { m ->
            AsyncImage(model = m.thumb, contentDescription = m.name)
            Spacer(Modifier.height(8.dp))
            Text(m.name)
            m.area?.let { Text(it) }
            m.category?.let { Text(it) }
            Spacer(Modifier.height(12.dp))
            Text("Ingredients")
            Spacer(Modifier.height(6.dp))
            m.ingredients().forEach { (ing, mea) ->
                Text("• $ing  ${if (mea.isBlank()) "" else "— $mea"}")
            }
            Spacer(Modifier.height(12.dp))
            Text("Instructions")
            Text(m.instructions.orEmpty())
        } ?: run {
            Text("Loading…")
        }
    }
}
