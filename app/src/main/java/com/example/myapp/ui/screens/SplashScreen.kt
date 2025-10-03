package com.example.myapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapp.ui.theme.MyAndroidAppTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDone: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val isPreview = LocalInspectionMode.current

    LaunchedEffect(isPreview) {
        visible = true
        if (!isPreview) {
            delay(1500)
            onDone()
        }
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(visible = visible, enter = fadeIn() + scaleIn(initialScale = 0.9f)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Reciping",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "by Hsu Chia Chang",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Splash")
@Composable
fun SplashScreenPreview() {
    MyAndroidAppTheme { SplashScreen(onDone = {}) }
}
