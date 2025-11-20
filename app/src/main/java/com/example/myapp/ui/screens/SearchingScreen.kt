package com.example.myapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.myapp.data.remote.MealSummary
import com.example.myapp.ui.viewmodel.SearchingViewModel

@Composable
fun SearchScreen(
    onMealClick: (String) -> Unit,
    vm: SearchingViewModel = viewModel()
) {
    val results by vm.results.collectAsState()
    var tf by remember { mutableStateOf(TextFieldValue("")) }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        OutlinedTextField(
            value = tf,
            onValueChange = {
                tf = it
                vm.setQuery(it.text)
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search recipe") },
            singleLine = true
        )
        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(results, key = { it.id }) { m ->
                SearchRow(m) { onMealClick(m.id) }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun SearchRow(meal: MealSummary, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(meal.thumb),
            contentDescription = meal.name,
            modifier = Modifier.size(72.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(meal.name)
            meal.area?.let { Text(it) }
            meal.category?.let { Text(it) }
        }
    }
}
