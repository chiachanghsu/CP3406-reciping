package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapp.data.remote.Meal
import com.example.myapp.ui.theme.MyAndroidAppTheme
import com.example.myapp.ui.viewmodel.SearchNetworkViewModel

@Composable
fun SearchScreen() {
    val vm: SearchNetworkViewModel = viewModel()
    val query by vm.query.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()
    val results by vm.results.collectAsState()

    SearchContent(
        query = query,
        onQueryChange = vm::setQuery,
        loading = loading,
        error = error,
        results = results
    )
}

@Composable
private fun SearchContent(
    query: String,
    onQueryChange: (String) -> Unit,
    loading: Boolean,
    error: String?,
    results: List<Meal>
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search online recipes…") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
        )

        Spacer(Modifier.height(12.dp))

        when {
            query.isBlank() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Type a keyword (e.g. “chicken”, “beef”, “pasta”…)")
                }
            }
            loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: $error")
                }
            }
            results.isEmpty() -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No results")
                }
            }
            else -> {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(results) { meal ->
                        MealRow(meal)
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun MealRow(meal: Meal) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = meal.strMealThumb,
            contentDescription = meal.strMeal,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(meal.strMeal, style = MaterialTheme.typography.titleMedium)
            val meta = listOfNotNull(meal.strArea, meal.strCategory).joinToString(" • ")
            if (meta.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(meta, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SearchPreview() {
    MyAndroidAppTheme {
        SearchContent(
            query = "chicken",
            onQueryChange = {},
            loading = false,
            error = null,
            results = listOf(
                Meal(id = "1", strMeal = "Chicken Handi", strMealThumb = null, strArea = "Indian", strCategory = "Chicken"),
                Meal(id = "2", strMeal = "Chicken Alfredo", strMealThumb = null, strArea = "Italian", strCategory = "Pasta")
            )
        )
    }
}
