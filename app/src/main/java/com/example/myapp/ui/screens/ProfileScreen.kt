package com.example.myapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.ui.theme.MyAndroidAppTheme

@Composable
fun ProfileScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Profile page")
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Profile")
@Composable
fun ProfileScreenPreview() {
    MyAndroidAppTheme { ProfileScreen() }
}
