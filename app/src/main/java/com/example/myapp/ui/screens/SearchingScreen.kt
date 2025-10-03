package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapp.ui.theme.MyAndroidAppTheme

@Composable
fun SearchScreen() {
    var query by rememberSaveable { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
            placeholder = { Text("Search recipes…") },
            singleLine = true
        )

        Spacer(Modifier.height(24.dp))

        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(if (query.isBlank()) "" else "Searching for \"$query\"")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Search")
@Composable
fun SearchScreenPreview() {
    MyAndroidAppTheme { SearchScreen() }
}
