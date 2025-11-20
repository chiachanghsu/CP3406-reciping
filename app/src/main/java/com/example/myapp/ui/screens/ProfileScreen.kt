package com.example.myapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapp.ui.viewmodel.DetailViewModel
import com.example.myapp.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    onMealClick: (String) -> Unit,
    vm: ProfileViewModel = viewModel()
) {
    val saved by vm.saved.collectAsState(initial = emptyList())
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(12.dp)) {
        items(saved, key = { it.id }) { s ->
            ListItem(
                leadingContent = { AsyncImage(model = s.thumb, contentDescription = s.name, modifier = Modifier.size(48.dp)) },
                headlineContent = { Text(s.name) },
                supportingContent = {
                    val meta = listOfNotNull(s.category, s.area).joinToString(" · ")
                    if (meta.isNotBlank()) Text(meta)
                },
                modifier = Modifier.clickable { onMealClick(s.id) }
            )
            Divider()
        }
    }
}
