package com.example.myapp.ui.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.R
import com.example.myapp.ui.theme.MyAndroidAppTheme
import com.example.myapp.ui.viewmodel.WordViewModel

@Composable
fun HomeScreen() {
    val app = LocalContext.current.applicationContext as Application
    val vm: WordViewModel = viewModel(factory = object : ViewModelProvider.AndroidViewModelFactory(app) {})
    val words by vm.words.collectAsState()

    Column(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.food_1),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(220.dp),
            contentScale = ContentScale.Crop
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            item {
                Text("Saved words", modifier = Modifier.padding(bottom = 8.dp))
                Divider()
            }
            items(words) { w ->
                ListItem(headlineContent = { Text(w.text) })
                Divider()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Home")
@Composable
fun HomeScreenPreview() {
    MyAndroidAppTheme {
        Column(Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.food_1),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(220.dp),
                contentScale = ContentScale.Crop
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                item {
                    Text("Saved words", modifier = Modifier.padding(bottom = 8.dp))
                    Divider()
                }
                items(listOf("Apple", "Banana", "Carrot")) { t ->
                    ListItem(headlineContent = { Text(t) })
                    Divider()
                }
            }
        }
    }
}
