package com.example.myapp.ui.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapp.data.remote.Meal
import com.example.myapp.ui.theme.MyAndroidAppTheme
import com.example.myapp.ui.viewmodel.HomeNetViewModel

@Composable
fun HomeScreen() {
    val app = LocalContext.current.applicationContext as Application
    val vm: HomeNetViewModel =
        viewModel(factory = object : ViewModelProvider.AndroidViewModelFactory(app) {})

    val meals by vm.meals.collectAsState()
    val loading by vm.loading.collectAsState()
    val error by vm.error.collectAsState()

    Box(Modifier.fillMaxSize()) {
        when {
            loading -> {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
            error != null -> {
                Text("Error: $error", Modifier.align(Alignment.Center))
            }
            meals.isEmpty() -> {
                Text("No recipes found.", Modifier.align(Alignment.Center))
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(meals) { m -> MealRow(m) }
                }
            }
        }
    }
}

@Composable
private fun MealRow(m: Meal) {
    ListItem(
        leadingContent = {
            if (!m.strMealThumb.isNullOrBlank()) {
                AsyncImage(
                    model = m.strMealThumb,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    contentScale = ContentScale.Crop
                )
            }
        },
        headlineContent = { Text(m.strMeal) },
        supportingContent = {
            val meta = listOfNotNull(m.strCategory, m.strArea).joinToString(" • ")
            if (meta.isNotBlank()) Text(meta)
        }
    )
    Divider()
}

@Preview(showBackground = true, showSystemUi = true, name = "Home (loading)")
@Composable
fun HomeScreenPreview() {
    MyAndroidAppTheme {
        // Preview won’t hit the network; leave empty.
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}
