package com.example.myapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.myapp.data.remote.MealSummary
import com.example.myapp.ui.viewmodel.HomeRandomViewModel

@Composable
fun HomeScreen(
    onMealClick: (String) -> Unit,
    vm: HomeRandomViewModel = viewModel()
) {
    // fill on first entry
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val obs = LifecycleEventObserver { _, ev ->
            if (ev == Lifecycle.Event.ON_RESUME) vm.ensureInitial()
        }
        lifecycleOwner.lifecycle.addObserver(obs)
        onDispose { lifecycleOwner.lifecycle.removeObserver(obs) }
    }

    val meals by vm.meals.collectAsState()
    val loading by vm.loading.collectAsState()

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(12.dp)
        ) {
            items(items = meals, key = { it.id }) { m ->
                MealCard(m, onClick = { onMealClick(m.id) })
                Spacer(Modifier.height(12.dp))
            }

            // infinite scroll: ask for one more when we reach the end
            item {
                LaunchedEffect(meals.size) {
                    vm.loadMoreOne()
                }
            }
        }

        if (loading && meals.isEmpty()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun MealCard(meal: MealSummary, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(12.dp)) {
            Image(
                painter = rememberAsyncImagePainter(meal.thumb),
                contentDescription = meal.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(meal.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            meal.area?.let { Text(it) }
            meal.category?.let { Text(it) }
        }
    }
}
