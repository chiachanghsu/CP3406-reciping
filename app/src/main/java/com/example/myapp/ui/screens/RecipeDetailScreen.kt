package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapp.data.remote.MealDetail
import com.example.myapp.ui.viewmodel.DetailViewModel

@Composable
fun RecipeDetailScreen(mealId: String, vm: DetailViewModel = viewModel()) {
    LaunchedEffect(mealId) { vm.load(mealId) }

    val meal by vm.meal.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    when {
        loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
        error != null -> Text("Error: $error", color = MaterialTheme.colorScheme.error)
        meal != null -> DetailContent(meal!!)
        else -> Text("No data")
    }
}

@Composable
private fun DetailContent(m: MealDetail) {
    val ingredients = remember(m) { extractIngredients(m) }
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(m.strMeal, style = MaterialTheme.typography.headlineSmall)
        if (!m.strMealThumb.isNullOrBlank())
            AsyncImage(model = m.strMealThumb, contentDescription = m.strMeal)
        val meta = listOfNotNull(m.strArea, m.strCategory).joinToString(" • ")
        if (meta.isNotBlank()) Text(meta)

        Divider()
        Text("Ingredients", style = MaterialTheme.typography.titleMedium)
        ingredients.forEach { (name, measure) ->
            Text("• $name${if (measure.isNotBlank()) " — $measure" else ""}")
        }

        Divider()
        Text("Instructions", style = MaterialTheme.typography.titleMedium)
        Text(m.strInstructions ?: "No instructions.")
    }
}

/** Pull non-empty ingredient/measure pairs. */
private fun extractIngredients(m: MealDetail): List<Pair<String, String>> {
    val pairs = listOf(
        m.strIngredient1  to m.strMeasure1,   m.strIngredient2  to m.strMeasure2,
        m.strIngredient3  to m.strMeasure3,   m.strIngredient4  to m.strMeasure4,
        m.strIngredient5  to m.strMeasure5,   m.strIngredient6  to m.strMeasure6,
        m.strIngredient7  to m.strMeasure7,   m.strIngredient8  to m.strMeasure8,
        m.strIngredient9  to m.strMeasure9,   m.strIngredient10 to m.strMeasure10,
        m.strIngredient11 to m.strMeasure11,  m.strIngredient12 to m.strMeasure12,
        m.strIngredient13 to m.strMeasure13,  m.strIngredient14 to m.strMeasure14,
        m.strIngredient15 to m.strMeasure15,  m.strIngredient16 to m.strMeasure16,
        m.strIngredient17 to m.strMeasure17,  m.strIngredient18 to m.strMeasure18,
        m.strIngredient19 to m.strMeasure19,  m.strIngredient20 to m.strMeasure20,
    )
    return pairs.filter { !(it.first.isNullOrBlank()) }
        .map { it.first!!.trim() to (it.second ?: "").trim() }
}
