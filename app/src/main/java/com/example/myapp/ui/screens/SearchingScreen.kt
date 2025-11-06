package com.example.myapp.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.data.remote.Meal
import com.example.myapp.ui.theme.MyAndroidAppTheme
import com.example.myapp.ui.viewmodel.SearchNetworkViewModel
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun SearchScreen() {
    val app = LocalContext.current.applicationContext as Application
    val vm: SearchNetworkViewModel = viewModel(
        factory = object : ViewModelProvider.AndroidViewModelFactory(app) {}
    )

    val query by vm.query.collectAsState()
    val results by vm.results.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    SearchContent(
        query = query,
        onQueryChange = { vm.setQuery(it) },
        results = results,
        loading = loading,
        error = error
    )
}

@Composable
private fun SearchContent(
    query: String,
    onQueryChange: (String) -> Unit,
    results: List<Meal>,
    loading: Boolean,
    error: String?
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search recipes…") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
        )
        Spacer(Modifier.height(12.dp))

        when {
            query.isBlank() -> {
                // keep empty
            }
            loading -> {
                Text("Searching…", modifier = Modifier.padding(8.dp))
            }
            error != null -> {
                Text("Error: $error", modifier = Modifier.padding(8.dp))
            }
            results.isEmpty() -> {
                Text("No results", modifier = Modifier.padding(8.dp))
            }
            else -> {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(results) { m ->
                        ListItem(
                            headlineContent = { Text(m.strMeal) },
                            supportingContent = {
                                val sub = buildString {
                                    if (!m.strArea.isNullOrBlank()) append(m.strArea)
                                    if (!m.strCategory.isNullOrBlank()) {
                                        if (isNotEmpty()) append(" • ")
                                        append(m.strCategory)
                                    }
                                }
                                if (sub.isNotBlank()) Text(sub)
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SearchPreview() {
    MyAndroidAppTheme {
        SearchContent(
            query = "",
            onQueryChange = {},
            results = emptyList(),
            loading = false,
            error = null
        )
    }
}
