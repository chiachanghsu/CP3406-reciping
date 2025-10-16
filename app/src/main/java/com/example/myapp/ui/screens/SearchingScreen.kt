package com.example.myapp.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.data.model.Recipe
import com.example.myapp.ui.theme.MyAndroidAppTheme
import com.example.myapp.ui.viewmodel.SearchViewModel
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun SearchScreen() {
    val app = LocalContext.current.applicationContext as Application
    val vm: SearchViewModel = viewModel(factory = object : ViewModelProvider.AndroidViewModelFactory(app) {})
    val query by vm.query.collectAsState()
    val results by vm.results.collectAsState()

    SearchContent(
        query = query,
        onQueryChange = { vm.query.value = it },
        results = results
    )
}

@Composable
private fun SearchContent(
    query: String,
    onQueryChange: (String) -> Unit,
    results: List<Recipe>
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search recipes or ingredients…") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
        )
        Spacer(Modifier.height(12.dp))
        LazyColumn(Modifier.fillMaxSize()) {
            if (results.isEmpty()) {
                item {
                    Text("No results", modifier = Modifier.padding(8.dp))
                }
            } else {
                items(results) { r ->
                    ListItem(
                        headlineContent = { Text(r.name) },
                        supportingContent = {
                            Text("~${r.timeMinutes} min • ${r.ingredients.lines().firstOrNull()?.trim() ?: ""}…")
                        }
                    )
                    Divider()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Search (Preview)")
@Composable
private fun SearchPreview() {
    MyAndroidAppTheme {
        SearchContent(
            query = "pan",
            onQueryChange = {},
            results = listOf(
                Recipe(name = "Fluffy Pancakes", ingredients = "Flour\nMilk\nEgg…", timeMinutes = 25),
                Recipe(name = "Pan-seared Salmon", ingredients = "Salmon\nButter\nGarlic…", timeMinutes = 18)
            )
        )
    }
}
