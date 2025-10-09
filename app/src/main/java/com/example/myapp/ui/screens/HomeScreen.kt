package com.example.myapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapp.R
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.ui.theme.MyAndroidAppTheme

@Composable
fun HomeScreen() {
    Column(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.food_1),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Home")
@Composable
fun HomeScreenPreview() {
    MyAndroidAppTheme { HomeScreen() }
}